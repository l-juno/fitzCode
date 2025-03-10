package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.common.dto.AdminOrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminOrderMapper {

    List<AdminOrderDTO> getOrderList(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("status") Integer status,
            @Param("sortBy") String sortBy);

    int getTotalOrderCount(@Param("status") Integer status);
}