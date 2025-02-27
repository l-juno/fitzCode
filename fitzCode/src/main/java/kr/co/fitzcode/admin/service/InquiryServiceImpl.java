package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.InquiryDTO;
import kr.co.fitzcode.admin.mapper.InquiryMapper;
import kr.co.fitzcode.common.enums.InquiryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final InquiryMapper inquiryMapper;

    /**
     * 미답변 문의 개수 조회
     * @return 답변이 등록되지 않은 문의 개수
     */
    @Override
    public int getUnansweredInquiryCount() {
        return inquiryMapper.getUnansweredInquiryCount();
    }

    /**
     * 문의 목록 조회 (페이징 처리)
     * @param page 조회할 페이지 번호
     * @param size 페이지당 조회할 문의 개수
     * @return 문의 목록 리스트
     */
    @Override
    public List<InquiryDTO> getInquiryList(int page, int size) {
        int offset = (page - 1) * size;
        return inquiryMapper.getInquiryList(size, offset);
    }

    /**
     * 특정 문의 상세 조회
     * @param inquiryId 조회할 문의 ID
     * @return 문의 상세 정보 DTO
     */
    @Override
    public InquiryDTO getInquiryDetail(int inquiryId) {
        return inquiryMapper.getInquiryDetail(inquiryId);
    }

    /**
     * 문의 상태 변경
     * @param inquiryId 상태를 변경할 문의 ID
     * @param status 변경할 상태 코드
     */
    @Override
    public void updateInquiryStatus(int inquiryId, int status) {
        inquiryMapper.updateInquiryStatus(inquiryId, status);
    }

    /**
     * 문의 카테고리 변경
     * @param inquiryId 카테고리를 변경할 문의 ID
     * @param category 변경할 카테고리 코드
     */
    @Override
    public void updateInquiryCategory(int inquiryId, int category) {
        inquiryMapper.updateInquiryCategory(inquiryId, category);
    }

    /**
     * 문의 답변 작성
     * 답변이 등록되면 자동으로 상태를 '답변완료'로 변경
     * @param inquiryId 답변할 문의 ID
     * @param reply 답변 내용
     */
    @Override
    public void updateInquiryReply(int inquiryId, String reply) {
        // 답변 등록 시 상태를 ANSWERED(2)로 변경
        inquiryMapper.updateInquiryReplyAndStatus(inquiryId, reply, InquiryStatus.ANSWERED.getCode());
    }
}