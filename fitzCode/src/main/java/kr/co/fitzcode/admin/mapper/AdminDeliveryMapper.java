package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.common.dto.DeliveryDTO;
import kr.co.fitzcode.common.enums.DeliveryStatus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDeliveryMapper {

    void insertDelivery(DeliveryDTO delivery);

    DeliveryDTO getDeliveryByOrderId(Long orderId);

    void updateDeliveryStatus(Long deliveryId, DeliveryStatus status);
}