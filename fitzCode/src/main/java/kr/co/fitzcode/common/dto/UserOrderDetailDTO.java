package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.common.enums.OrderStatus;
import kr.co.fitzcode.common.enums.ProductSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrderDetailDTO {

    private int orderDetailId;
    private int orderId;
    private int productId;
    private int quantity;
    private int price;
    private int couponId;
    private int refundStatus;
    private int sizeCode;
    private String imageUrl;
    private String productName;

    public String getFormattedRefundStatus() {
        return OrderStatus.fromCode(refundStatus).getDescription();
    }

    public String getFormattedSizeCode() {
        return ProductSize.fromCode(sizeCode).getDescription();
    }

    public String getFormattedPrice() {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(price);
    }

}
