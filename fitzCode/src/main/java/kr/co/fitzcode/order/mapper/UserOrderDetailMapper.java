package kr.co.fitzcode.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserOrderDetailMapper {

    void batchInsertOrderDetails(@Param("orderDetails") List<Map<String, Object>> orderDetails);


}
