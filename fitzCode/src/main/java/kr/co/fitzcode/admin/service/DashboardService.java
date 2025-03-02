package kr.co.fitzcode.admin.service;

import java.util.Map;

public interface DashboardService {
    // 1대1 문의 관련
    Map<String, Integer> getInquiryStatusCounts();
}