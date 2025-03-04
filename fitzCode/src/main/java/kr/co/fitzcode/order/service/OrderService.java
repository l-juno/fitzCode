package kr.co.fitzcode.order.service;

import java.util.HashMap;
import java.util.List;

public interface OrderService {
    List<HashMap<String, Object>> OrderList(int userId);
}
