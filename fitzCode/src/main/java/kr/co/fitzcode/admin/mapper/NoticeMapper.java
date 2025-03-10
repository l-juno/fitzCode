package kr.co.fitzcode.admin.mapper;


import kr.co.fitzcode.common.dto.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

    // 공지사항 목록 조회
    List<NoticeDTO> selectAllNotices();

    // 공지사항 작성
    void insertNotice(NoticeDTO noticeDTO);

    // 공지사항 조회
    NoticeDTO selectNoticeById(int noticeId);

    // 공지사항 수정
    void updateNotice(NoticeDTO noticeDTO);

    // 공지사항 삭제
    void deleteNotice(int noticeId);
}