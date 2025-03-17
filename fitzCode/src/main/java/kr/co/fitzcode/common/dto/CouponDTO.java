package kr.co.fitzcode.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class CouponDTO {
    private int couponId;                   // 쿠폰 아이디
    private String couponCode;              // 쿠폰 코드
    private String description;             // 쿠폰 설명
    private int discountAmount;             // 할인 금액
    private int discountPercentage;         // 할인 비율
    private Integer minimumOrderAmount;     // 최소주문 금액
    private Timestamp validUntil;           // 사용가능 기한 (USER_COUPON)
    private int couponType;                 // 쿠폰 유형 (1: 금액할인, 2: 비율할인, 3: 무료배송, 4: 첫주문할인)
    private Timestamp expiryDate;           // 만료 날짜 (COUPON 테이블의 expiry_date)
    private Boolean isActive;               // 쿠폰 활성화 여부
    private Timestamp createdAt;            // 생성 날짜

    // 최소주문금액
    public String getFormattedMinimumOrderAmount() {
        DecimalFormat df = new DecimalFormat("#,###원");
        return df.format(minimumOrderAmount);
    }

    // 사용가능기한
    public String getFormattedValidUntil() {
        if (validUntil != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(validUntil);
        }
        return "";
    }

    // 만료 날짜
    public String getFormattedExpiryDate() {
        if (expiryDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(expiryDate);
        }
        return "";
    }

    // 생성 날짜
    public String getFormattedCreatedAt() {
        if (createdAt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(createdAt);
        }
        return "";
    }
}