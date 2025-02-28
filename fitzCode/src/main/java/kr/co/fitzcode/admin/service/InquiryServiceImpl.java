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

    @Override
    public int getUnansweredInquiryCount() {
        return inquiryMapper.getUnansweredInquiryCount();
    }

    @Override
    public int getTotalInquiryCount(List<Integer> categoryIds, List<Integer> statusCodes) {
        return inquiryMapper.getTotalFilteredInquiryCount(categoryIds, statusCodes);
    }

    @Override
    public List<InquiryDTO> getInquiryList(int page, int size, List<Integer> categoryIds, List<Integer> statusCodes) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;

        int offset = (page - 1) * size;
        return inquiryMapper.getFilteredInquiryList(size, offset, categoryIds, statusCodes);
    }

    @Override
    public int calculateTotalPages(int totalCount, int size) {
        if (size < 1) size = 10; // 기본값 보장
        return Math.max(1, (int) Math.ceil((double) totalCount / size)); // 최소 1페이지 보장
    }

    @Override
    public InquiryDTO getInquiryDetail(int inquiryId) {
        return inquiryMapper.getInquiryDetail(inquiryId);
    }

    @Override
    public void updateInquiryStatus(int inquiryId, int status) {
        inquiryMapper.updateInquiryStatus(inquiryId, status);
    }

    @Override
    public void updateInquiryCategory(int inquiryId, int category) {
        inquiryMapper.updateInquiryCategory(inquiryId, category);
    }

    @Override
    public void updateInquiryReply(int inquiryId, String reply) {
        inquiryMapper.updateInquiryReplyAndStatus(inquiryId, reply, InquiryStatus.ANSWERED.getCode());
    }
}