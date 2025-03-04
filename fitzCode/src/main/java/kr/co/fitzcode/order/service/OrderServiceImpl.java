package kr.co.fitzcode.order.service;

import kr.co.fitzcode.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderMapper orderMapper;

    @Override
    public List<HashMap<String, Object>> OrderList(int userId) {
        // 가져오는 값 :  o.order_id, o.order_status, o.created_at, d.tracking_number
        List<HashMap<String, Object>> list = orderMapper.getOderListByUserId(userId);
        // 우선 리스트로 갖고와야하나 ?

        return list;
    }
}
