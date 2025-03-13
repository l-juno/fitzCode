package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.common.dto.SalesRankingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface AdminSalesReportMapper {
    List<Map<String, Object>> getDailySalesData(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    List<SalesRankingDTO> getSalesRankingForLast14Days(@Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate,
                                                       @Param("offset") int offset);

    int getTotalSalesRankingCount(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);
}