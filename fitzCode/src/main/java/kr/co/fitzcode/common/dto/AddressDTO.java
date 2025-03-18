package kr.co.fitzcode.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "주소 정보")
@Builder
public class AddressDTO {
    @Schema(description = "사용자 ID")
    private int userId;

    @Schema(description = "주소 ID")
    private int addressId;

    @Schema(description = "주소1")
    private String addressLine1;

    @Schema(description = "주소2")
    private String addressLine2;

    @Schema(description = "우편번호")
    private String postalCode;

    @Schema(description = "기본 배송지 여부")
    private Boolean isDefault;

    @Schema(description = "기본 배송지 주소1")
    private String defaultAddressLine1;

    @Schema(description = "기본 배송지 주소2")
    private String defaultAddressLine2;

    @Schema(description = "기본 배송지 우편번호")
    private String defaultPostalCode;



    public boolean isDefault() {
        return isDefault;
    }
}