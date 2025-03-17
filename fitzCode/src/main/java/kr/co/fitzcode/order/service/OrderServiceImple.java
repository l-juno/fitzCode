package kr.co.fitzcode.order.service;

import kr.co.fitzcode.common.dto.AddressDTO;
import kr.co.fitzcode.common.dto.OrderDTO;
import kr.co.fitzcode.common.dto.PaymentDTO;
import kr.co.fitzcode.order.mapper.UserOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImple implements OrderService {

    private final UserOrderMapper userOrderMapper;


    @Override
    public List<AddressDTO> getUserAddress(int userId) {
        return userOrderMapper.getUserAddressByUserId(userId);
    }

    @Override
    public boolean checkIfAddressExistsForUser(int userId, String addressLine1, String postalCode) {
        return userOrderMapper.checkIfAddressExistsForUser(userId, addressLine1, postalCode);
    }

    @Override
    public void addNonDefaultAddressForUser(int userId, AddressDTO addressDTO) {
        userOrderMapper.addNonDefaultAddressForUser(userId, addressDTO);
    }

    @Override
    public int getAddressIdUsingAddressLine1AndPostalCode(String addressLine1, String postalCode, int userId) {
        return userOrderMapper.getAddressIdUsingAddressLine1AndPostalCode(addressLine1, postalCode, userId);
    }

    @Override
    public int insertNewOrder(OrderDTO orderDTO) {
        userOrderMapper.insertNewOrder(orderDTO);
        return orderDTO.getOrderId();
    }

    @Override
    public void addPayment(PaymentDTO paymentDTO) {
        userOrderMapper.addPayment(paymentDTO);
    }
}
