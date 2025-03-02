package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.DashboardMapper;
import kr.co.fitzcode.common.enums.InquiryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMapper dashboardMapper;

    @Override
    public Map<String, Integer> getInquiryStatusCounts() {
        Map<String, Integer> counts = new HashMap<>();
        for (InquiryStatus status : InquiryStatus.values()) {
            int count = dashboardMapper.countByStatus(status.getCode());
            counts.put(status.name(), count); // 상태 이름으로 키 사용핰 ("PENDING", "ANSWERED", "CLOSED")
        }
        return counts;
    }
}