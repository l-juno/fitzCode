package kr.co.fitzcode.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private int userId;
    private int addressId;
    private String addressLine1; // 주소1
    private String addressLine2; // 주소2
    private String postalCode;   // 우편번호


    private String defaultAddressLine1;     // 기본 배송지 주소1
    private String defaultAddressLine2;     // 기본 배송지 주소2
    private String defaultPostalCode;       // 기본 배송지 우편번호
    private Boolean isDefault;              // 기본 배송지 여부

    public boolean isDefault() {
        return isDefault;
    }
}