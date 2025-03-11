package kr.co.fitzcode.common.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private int addressId;
    private String addressLine1; // 주소1
    private String addressLine2; // 주소2
    private String postalCode;   // 우편번호
}