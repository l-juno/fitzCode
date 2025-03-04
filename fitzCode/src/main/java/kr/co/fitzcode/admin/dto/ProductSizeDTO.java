package kr.co.fitzcode.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSizeDTO {
    private Long productSizeId;
    private Long productId;
    private Integer sizeCode;
    private Integer stock;
}