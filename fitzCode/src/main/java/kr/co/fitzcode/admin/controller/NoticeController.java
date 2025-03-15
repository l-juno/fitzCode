package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.exception.InvalidNoticeIdException;
import kr.co.fitzcode.admin.exception.NoticeNotFoundException;
import kr.co.fitzcode.admin.service.NoticeService;
import kr.co.fitzcode.common.dto.CustomOAuth2User;
import kr.co.fitzcode.common.dto.NoticeDTO;
import kr.co.fitzcode.common.service.CustomUserDetails;
import kr.co.fitzcode.common.service.NotificationService;
import kr.co.fitzcode.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
@Slf4j
public class NoticeController {

    private final NoticeService noticeService;
    private final NotificationService notificationService;
    private final S3Service s3Service;
    private final software.amazon.awssdk.services.s3.S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        return notificationService.subscribe();
    }

    @PostMapping
    public String createNotice(@ModelAttribute NoticeDTO noticeDTO,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @RequestParam("attachmentFile") MultipartFile attachmentFile,
                               Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                throw new IllegalStateException("사용자가 인증되지 않았습니다.");
            }

            int userId = extractUserId(auth);
            noticeDTO.setUserId(userId);
            noticeService.createNotice(noticeDTO, imageFile, attachmentFile);
            log.info("공지사항 생성 완료 - 제목: {}, 공지사항 ID: {}", noticeDTO.getTitle(), noticeDTO.getNoticeId());
            log.info("NOTICE_CREATED 이벤트 전송 - 공지사항 DTO: {}", noticeDTO.getTitle());
            notificationService.sendNotificationToAll("NOTICE_CREATED", noticeDTO);
            return "redirect:/admin/notice";
        } catch (Exception e) {
            log.error("공지사항 생성 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "404-error";
        }
    }

    @GetMapping
    public String getAllNotices(Model model) {
        try {
            List<NoticeDTO> notices = noticeService.getAllNotices();
            model.addAttribute("notices", notices);
            return "admin/notice/list";
        } catch (NoticeNotFoundException e) {
            log.error("공지사항 목록 조회 실패: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "404-error";
        }
    }

    @GetMapping("/add")
    public String createNoticeForm(Model model) {
        model.addAttribute("notice", new NoticeDTO());
        return "admin/notice/create";
    }

    @GetMapping("/{id}")
    public String getNoticeById(@PathVariable int id, Model model) {
        try {
            NoticeDTO notice = noticeService.getNoticeById(id);
            model.addAttribute("notice", notice);
            return "admin/notice/detail";
        } catch (NoticeNotFoundException e) {
            log.error("공지사항 상세 조회 실패 - ID: {}, 메시지: {}", id, e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "404-error";
        }
    }

    @GetMapping("/{id}/edit")
    public String editNoticeForm(@PathVariable int id, Model model) {
        try {
            NoticeDTO notice = noticeService.getNoticeById(id);
            model.addAttribute("notice", notice);
            return "admin/notice/edit";
        } catch (NoticeNotFoundException e) {
            log.error("공지사항 수정 폼 조회 실패 - ID: {}, 메시지: {}", id, e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "404-error";
        }
    }

    @PostMapping("/{id}")
    public String updateNotice(@PathVariable int id,
                               @ModelAttribute NoticeDTO noticeDTO,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @RequestParam("attachmentFile") MultipartFile attachmentFile,
                               Model model) {
        try {
            noticeDTO.setNoticeId(id);
            noticeService.updateNotice(noticeDTO, imageFile, attachmentFile);
            return "redirect:/admin/notice";
        } catch (InvalidNoticeIdException e) {
            log.error("공지사항 업데이트 실패 - ID: {}, 메시지: {}", id, e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "404-error";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteNotice(@PathVariable int id, Model model) {
        try {
            noticeService.deleteNotice(id);
            return "redirect:/admin/notice";
        } catch (InvalidNoticeIdException e) {
            log.error("공지사항 삭제 실패 - ID: {}, 메시지: {}", id, e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "404-error";
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> downloadAttachment(@PathVariable int id) throws IOException {
        try {
            NoticeDTO notice = noticeService.getNoticeById(id);
            if (notice.getAttachmentUrl() == null || notice.getAttachmentUrl().isEmpty()) {
                throw new IllegalArgumentException("다운로드할 첨부 파일이 없습니다.");
            }

            String key = notice.getAttachmentUrl().replace("https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/", "");
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());

            String originalFilename = notice.getAttachmentName();
            String encodedFilename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
            String contentType = s3Object.response().contentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                    .body(new InputStreamResource(s3Object));
        } catch (Exception e) {
            log.error("첨부 파일 다운로드 실패 - ID: {}, 메시지: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/{id}/delete-image")
    @ResponseBody
    public Map<String, Object> deleteImage(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            noticeService.deleteImage(id);
            response.put("success", true);
            log.info("이미지 삭제 성공 - ID: {}", id);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            log.error("이미지 삭제 실패 - ID: {}, 메시지: {}", id, e.getMessage(), e);
        }
        return response;
    }

    @PostMapping("/{id}/delete-attachment")
    @ResponseBody
    public Map<String, Object> deleteAttachment(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            noticeService.deleteAttachment(id);
            response.put("success", true);
            log.info("첨부 파일 삭제 성공 - ID: {}", id);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            log.error("첨부 파일 삭제 실패 - ID: {}, 메시지: {}", id, e.getMessage(), e);
        }
        return response;
    }

    // Principal에서 userId를 추출하는 헬퍼 메서드
    private int extractUserId(Object principal) {
        Object principalObj = principal;

        // UsernamePasswordAuthenticationToken 또는 OAuth2AuthenticationToken 처리
        if (principalObj instanceof UsernamePasswordAuthenticationToken) {
            principalObj = ((UsernamePasswordAuthenticationToken) principalObj).getPrincipal();
        } else if (principalObj instanceof OAuth2AuthenticationToken) {
            principalObj = ((OAuth2AuthenticationToken) principalObj).getPrincipal();
        }

        // 내부 Principal 객체에서 userId 추출
        if (principalObj instanceof CustomUserDetails) {
            return ((CustomUserDetails) principalObj).getUserId();
        } else if (principalObj instanceof CustomOAuth2User) {
            return ((CustomOAuth2User) principalObj).getUserId();
        } else {
            log.error("알 수 없는 Principal 내부 타입: {}", principalObj.getClass().getName());
            throw new IllegalStateException("알 수 없는 Principal 내부 타입: " + principalObj.getClass().getName());
        }
    }
}