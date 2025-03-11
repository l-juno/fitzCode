package kr.co.fitzcode.order.service;

import kr.co.fitzcode.order.mapper.UserOrderDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOrderDetailServiceImple implements UserOrderDetailService {

    private final UserOrderDetailMapper userOrderDetailMapper;


    @Override
    public void addOrderSingleDetailToOrder(int orderId, List<Integer> productIdList) {
        userOrderDetailMapper.addOrderSingleDetailToOrder(orderId, productIdList);
    }
}
