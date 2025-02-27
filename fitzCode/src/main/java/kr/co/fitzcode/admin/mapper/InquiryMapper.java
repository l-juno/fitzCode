package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.admin.dto.InquiryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiryMapper {

    /**
     * 미답변 문의 개수 조회
     * @return 답변이 없는 문의 개수
     */
    int getUnansweredInquiryCount();

    /**
     * 문의 목록 조회 (페이징 처리)
     * @param limit 페이지당 조회할 문의 수
     * @param offset 조회 시작 위치 (limit * (page - 1))
     * @return 문의 목록 리스트
     */
    List<InquiryDTO> getInquiryList(@Param("limit") int limit, @Param("offset") int offset);

    /**
     * 특정 문의 상세 조회
     * @param inquiryId 조회할 문의 ID
     * @return 문의 상세 정보 DTO
     */
    InquiryDTO getInquiryDetail(@Param("inquiryId") int inquiryId);

    /**
     * 문의 상태 변경
     * @param inquiryId 상태를 변경할 문의 ID
     * @param status 변경할 상태 코드 (ex: 1 - 대기, 2 - 답변완료, 3 - 종료)
     */
    void updateInquiryStatus(@Param("inquiryId") int inquiryId, @Param("status") int status);

    /**
     * 문의 카테고리 변경
     * @param inquiryId 카테고리를 변경할 문의 ID
     * @param category 변경할 카테고리 코드
     */
    void updateInquiryCategory(@Param("inquiryId") int inquiryId, @Param("category") int category);

    /**
     * 문의 답변 작성 및 상태 변경
     * 답변이 등록되면 자동으로 상태를 '답변완료'로 변경
     * @param inquiryId 답변할 문의 ID
     * @param reply 답변 내용
     * @param status 변경할 상태 코드 (ex: 2 - 답변완료)
     */
    void updateInquiryReplyAndStatus(@Param("inquiryId") int inquiryId, @Param("reply") String reply, @Param("status") int status);
}