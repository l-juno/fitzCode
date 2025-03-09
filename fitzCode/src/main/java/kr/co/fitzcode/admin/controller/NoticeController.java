package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.dto.NoticeDTO;
import kr.co.fitzcode.admin.service.NoticeService;
import kr.co.fitzcode.admin.exception.NoticeNotFoundException;
import kr.co.fitzcode.admin.exception.InvalidNoticeIdException;
import kr.co.fitzcode.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
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
public class NoticeController {

    private final NoticeService noticeService;
    private final S3Client s3Client;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    // 공지사항 목록 조회
    @GetMapping
    public String getAllNotices(Model model) {
        try {
            List<NoticeDTO> notices = noticeService.getAllNotices();
            model.addAttribute("notices", notices);
            return "admin/notice/list";
        } catch (NoticeNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "/errorPage";
        }
    }

    // 공지사항 작성 폼
    @GetMapping("/add")
    public String createNoticeForm(Model model) {
        model.addAttribute("notice", new NoticeDTO());
        return "admin/notice/create";
    }

    // 공지사항 작성 처리
    @PostMapping
    public String createNotice(@ModelAttribute NoticeDTO noticeDTO,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @RequestParam("attachmentFile") MultipartFile attachmentFile,
                               Model model) {
        try {
            noticeDTO.setUserId(1); // 임시 userId 설정
            noticeService.createNotice(noticeDTO, imageFile, attachmentFile);
            return "redirect:/admin/notice";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "/errorPage";
        }
    }

    // 공지사항 상세 조회
    @GetMapping("/{id}")
    public String getNoticeById(@PathVariable int id, Model model) {
        try {
            NoticeDTO notice = noticeService.getNoticeById(id);
            model.addAttribute("notice", notice);
            return "admin/notice/detail";
        } catch (NoticeNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage";
        }
    }

    // 공지사항 수정 폼
    @GetMapping("/{id}/edit")
    public String editNoticeForm(@PathVariable int id, Model model) {
        try {
            NoticeDTO notice = noticeService.getNoticeById(id);
            model.addAttribute("notice", notice);
            return "admin/notice/edit";
        } catch (NoticeNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "/errorPage";
        }
    }

    // 공지사항 수정 처리
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
            model.addAttribute("error", e.getMessage());
            return "/errorPage";
        }
    }

    // 공지사항 삭제
    @GetMapping("/{id}/delete")
    public String deleteNotice(@PathVariable int id, Model model) {
        try {
            noticeService.deleteNotice(id);
            return "redirect:/admin/notice";
        } catch (InvalidNoticeIdException e) {
            model.addAttribute("error", e.getMessage());
            return "/errorPage";
        }
    }

    // 첨부 파일 다운로드
    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> downloadAttachment(@PathVariable int id) throws IOException {
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
    }

    // 이미지 삭제
    @PostMapping("/{id}/delete-image")
    @ResponseBody
    public Map<String, Object> deleteImage(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            noticeService.deleteImage(id);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // 첨부 파일 삭제
    @PostMapping("/{id}/delete-attachment")
    @ResponseBody
    public Map<String, Object> deleteAttachment(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            noticeService.deleteAttachment(id);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
}