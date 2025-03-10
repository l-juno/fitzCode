package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.common.dto.AdminOrderDTO;
import kr.co.fitzcode.common.dto.AdminOrderDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminOrderMapper {

    // 주문 목록 조회
    List<AdminOrderDTO> getOrderList(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("status") Integer status,
            @Param("sortBy") String sortBy);

    // 특정 상태 총 주문 개수 조회
    int getTotalOrderCount(@Param("status") Integer status);

    // 주문 상세 정보 조회
    AdminOrderDetailDTO getOrderDetail(@Param("orderId") Long orderId);
}