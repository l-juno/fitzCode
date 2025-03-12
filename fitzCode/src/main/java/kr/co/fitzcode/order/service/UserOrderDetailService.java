package kr.co.fitzcode.order.service;

import java.util.List;
import java.util.Map;

public interface UserOrderDetailService {
    void addOrderDetailToOrder(List<Map<String, Object>> batchInsertList);

}

