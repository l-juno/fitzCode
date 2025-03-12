package kr.co.fitzcode.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
    private int couponId;
    private String couponCode;
    private String description;
    private int couponType;
    private int discountAmount;
    private int discountPercentage;
    private int minimumOrderAmount;
    private LocalDateTime expiryDate;
    private boolean isActive;
    private LocalDateTime createdAt;



}
