package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.InquiryDTO;
import java.util.List;

public interface InquiryService {
    int getUnansweredInquiryCount();

    List<InquiryDTO> getInquiryList(int page, int size, List<Integer> categoryIds, List<Integer> statusCodes); // 필터링 기능 추가

    int getTotalInquiryCount(List<Integer> categoryIds, List<Integer> statusCodes); // 필터링된 총 개수

    int calculateTotalPages(int totalCount, int size);

    InquiryDTO getInquiryDetail(int inquiryId);

    void updateInquiryStatus(int inquiryId, int status);

    void updateInquiryCategory(int inquiryId, int category);

    void updateInquiryReply(int inquiryId, String reply);
}