package kr.co.fitzcode.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {
    private int imageId;
    private int productId;
    private String imageUrl;
    private int imageOrder;
}
