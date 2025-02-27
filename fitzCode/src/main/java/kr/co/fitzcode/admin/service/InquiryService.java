package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.InquiryDTO;
import java.util.List;

public interface InquiryService {

    /**
     * 미답변 문의 개수 조회
     * @return 답변이 등록되지 않은 문의 개수
     */
    int getUnansweredInquiryCount();

    /**
     * 문의 목록 조회 (페이징 처리)
     * @param page 조회할 페이지 번호
     * @param size 페이지당 조회할 문의 개수
     * @return 문의 목록 리스트
     */
    List<InquiryDTO> getInquiryList(int page, int size);

    /**
     * 특정 문의 상세 조회
     * @param inquiryId 조회할 문의 ID
     * @return 문의 상세 정보 DTO
     */
    InquiryDTO getInquiryDetail(int inquiryId);

    /**
     * 문의 상태 변경
     * @param inquiryId 상태를 변경할 문의 ID
     * @param status 변경할 상태 코드 (예: 1 - 대기, 2 - 답변완료, 3 - 종료)
     */
    void updateInquiryStatus(int inquiryId, int status);

    /**
     * 문의 카테고리 변경
     * @param inquiryId 카테고리를 변경할 문의 ID
     * @param category 변경할 카테고리 코드
     */
    void updateInquiryCategory(int inquiryId, int category);

    /**
     * 문의 답변 작성
     * 답변이 등록되면 자동으로 상태를 '답변완료'로 변경
     * @param inquiryId 답변할 문의 ID
     * @param reply 답변 내용
     */
    void updateInquiryReply(int inquiryId, String reply);
}