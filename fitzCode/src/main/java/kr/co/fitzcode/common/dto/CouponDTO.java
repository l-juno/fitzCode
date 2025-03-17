package kr.co.fitzcode.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Getter
@Setter
public class CouponDTO {
    private int couponId;                   // 쿠폰 아이디
    private String couponCode;              // 쿠폰 코드
    private String description;             // 쿠폰 설명
    private int discountAmount;             // 할인 금액
    private int discountPercentage;         // 할인 비율
    private Integer minimumOrderAmount;         // 최소주문 금액


    private Timestamp validUntil; // 사용가능 기한 (사용자 쿠폰)

    // 최소주문금액 포맷팅
    public String getFormattedMinimumOrderAmount() {
        DecimalFormat df = new DecimalFormat("#,###원");
        return df.format(minimumOrderAmount);
    }

    // 사용가능기한 포맷팅
    public String getFormattedValidUnitl() {
        if (validUntil != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(validUntil);
        }
        return "";
    }

}
