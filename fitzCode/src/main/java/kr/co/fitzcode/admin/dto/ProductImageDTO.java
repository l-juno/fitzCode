package kr.co.fitzcode.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageDTO {
    private Long productImageId;
    private Long productId;
    private String imageUrl; // 이미지 url
    private Integer imageOrder; // 이미지 순서
}
