package kr.co.fitzcode.order.service;

import java.util.List;

public interface UserOrderDetailService {
    void addOrderSingleDetailToOrder(int orderId, List<Integer> productIdList);
}
