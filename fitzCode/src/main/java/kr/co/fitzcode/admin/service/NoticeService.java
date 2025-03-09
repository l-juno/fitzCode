package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.NoticeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoticeService {
    // 공지사항 목록 조회
    List<NoticeDTO> getAllNotices();

    // 공지사항 작성
    void createNotice(NoticeDTO noticeDTO, MultipartFile imageFile, MultipartFile attachmentFile);

    // 공지사항 조회
    NoticeDTO getNoticeById(int noticeId);

    // 공지사항 수정
    void updateNotice(NoticeDTO noticeDTO, MultipartFile imageFile, MultipartFile attachmentFile);

    // 공지사항 삭제
    void deleteNotice(int noticeId);

    // 이미지 삭제
    void deleteImage(int noticeId);

    // 첨부 파일 삭제
    void deleteAttachment(int noticeId);
}