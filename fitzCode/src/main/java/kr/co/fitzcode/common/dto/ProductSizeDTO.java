package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.common.enums.ProductSize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSizeDTO {
    private Long productSizeId;
    private Long productId;
    private Integer sizeCode;
    private Integer stock;

    public String getSizeDescription() {
        if (sizeCode == null) return "알 수 없음";
        try {
            return ProductSize.fromCode(sizeCode).getDescription();
        } catch (IllegalArgumentException e) {
            return "알 수 없음";
        }
    }

    public String getSizeName() {
        return ProductSize.fromCode(this.sizeCode).getDescription();
    }
}