package kr.co.fitzcode.user.controller;

import kr.co.fitzcode.admin.service.NoticeService;
import kr.co.fitzcode.common.dto.NoticeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
@Slf4j
public class UserNoticeController {

    private final NoticeService noticeService;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    // 공지사항 리스트
    @GetMapping
    public String getAllNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        try {
            int pageIndex = page - 1;
            if (pageIndex < 0) {
                pageIndex = 0;
            }

            // 공지사항 목록
            List<NoticeDTO> notices = noticeService.getNoticesWithPagination(pageIndex, size);
            // 총 공지사항 수
            long totalNotices = noticeService.getTotalNoticeCount();
            // 총 페이지 수
            int totalPages = (int) Math.ceil((double) totalNotices / size);
            if (totalPages == 0) {
                totalPages = 1;
            }

            // 페이지 번호 목록
            int startPage = Math.max(1, page - 2);
            int endPage = Math.min(totalPages, startPage + 4);
            if (endPage - startPage < 4) {
                startPage = Math.max(1, endPage - 4);
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage)
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("notices", notices);
            model.addAttribute("currentPage", page);
            model.addAttribute("menuCurrentPage", "notice");
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("size", size);
            return "notice/list";
        } catch (Exception e) {
            log.error("공지사항 목록 조회 실패: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "404-error";
        }
    }

    // 공지사항 상세 페이지
    @GetMapping("/{id}")
    public String getNoticeById(@PathVariable int id, Model model) {
        try {
            NoticeDTO notice = noticeService.getNoticeById(id);
            model.addAttribute("notice", notice);
            model.addAttribute("menuCurrentPage", "notice");
            return "notice/detail";
        } catch (Exception e) {
            log.error("공지사항 상세 조회 실패 - ID: {}, 메시지: {}", id, e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "404-error";
        }
    }

    // 첨부 파일 다운로드
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
}