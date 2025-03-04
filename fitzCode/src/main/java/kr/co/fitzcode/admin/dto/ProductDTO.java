package kr.co.fitzcode.admin.dto;

import kr.co.fitzcode.common.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String description;
    private String brand;
    private Integer price;
    private Integer stock; // 총 재고 (PRODUCT_SIZE의 합계로 계산 가능)
    private Long categoryId;
    private String imageUrl; // 메인 이미지 URL
    private List<String> additionalImages; // 추가 이미지 URL 리스트
    private ProductStatus status; // Enum으로 상태 관리
    private Timestamp createdAt;
    private List<ProductSizeDTO> productSizes; // 사이즈별 재고

}