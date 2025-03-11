package kr.co.fitzcode.order.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserOrderDetailMapper {

    void addOrderSingleDetailToOrder(int orderId, List<Integer> productIdList);

}
