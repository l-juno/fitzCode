package kr.co.fitzcode.product.dto;

import kr.co.fitzcode.common.enums.ProductSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSizeDTO {
    private int productSizeId;
    private int productId;
    private int sizeCode;
    private int stock;


    public String getSizeName() {
        return ProductSize.fromCode(this.sizeCode).getDescription();
    }
}
