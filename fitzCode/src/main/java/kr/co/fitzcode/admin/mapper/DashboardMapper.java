package kr.co.fitzcode.admin.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DashboardMapper {
    // 1대1 문의 관련
    int countByStatus(int status);
}