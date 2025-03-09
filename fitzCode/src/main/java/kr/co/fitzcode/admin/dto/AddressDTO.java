package kr.co.fitzcode.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private String addressLine1; // 주소1
    private String addressLine2; // 주소2
    private String postalCode;   // 우편번호
}