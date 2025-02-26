package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.NoticeDTO;
import kr.co.fitzcode.admin.mapper.NoticeMapper;
import kr.co.fitzcode.admin.exception.NoticeNotFoundException;
import kr.co.fitzcode.admin.exception.InvalidNoticeIdException; // 추가한 예외 클래스 (예시)
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    // 공지사항 목록 조회
    @Override
    public List<NoticeDTO> getAllNotices() {
        return noticeMapper.selectAllNotices();
    }

    // 공지사항 작성
    @Override
    public void createNotice(NoticeDTO noticeDTO) {
        if (noticeDTO.getTitle() == null || noticeDTO.getContent() == null) {
            throw new IllegalArgumentException("제목과 내용은 필수입니다.");
        }
        noticeMapper.insertNotice(noticeDTO);
    }

    // 공지사항 조회
    @Override
    public NoticeDTO getNoticeById(int noticeId) {
        NoticeDTO notice = noticeMapper.selectNoticeById(noticeId);
        if (notice == null) {
            throw new NoticeNotFoundException("해당 공지사항을 찾을 수 없습니다. ID: " + noticeId);
        }
        return notice;
    }

    // 공지사항 수정
    @Override
    public void updateNotice(NoticeDTO noticeDTO) {
        if (noticeDTO.getNoticeId() <= 0) {
            throw new InvalidNoticeIdException("유효한 공지사항 ID가 필요합니다.");
        }
        noticeMapper.updateNotice(noticeDTO);
    }

    // 공지사항 삭제
    @Override
    public void deleteNotice(int noticeId) {
        if (noticeId <= 0) {
            throw new InvalidNoticeIdException("유효한 공지사항 ID가 필요합니다.");
        }
        noticeMapper.deleteNotice(noticeId);
    }
}