package kr.co.fitzcode.order.mapper;

import kr.co.fitzcode.common.dto.AddressDTO;
import kr.co.fitzcode.common.dto.OrderDTO;
import kr.co.fitzcode.common.dto.PaymentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserOrderMapper {

    List<AddressDTO> getUserAddressByUserId(int userId);

    boolean checkIfAddressExistsForUser(int userId, String addressLine1, String postalCode);

    void addNonDefaultAddressForUser(@Param("userId") int userId, @Param("addressDTO") AddressDTO addressDTO);

    int getAddressIdUsingAddressLine1AndPostalCode(String addressLine1, String postalCode, int userId);

    int insertNewOrder(OrderDTO orderDTO);

    void addPayment(@Param("paymentDTO") PaymentDTO paymentDTO);
}

