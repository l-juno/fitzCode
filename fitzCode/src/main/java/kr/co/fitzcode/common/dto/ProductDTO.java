package kr.co.fitzcode.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fitzcode.common.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상품 정보")
public class ProductDTO {
    @Schema(description = "상품 ID")
    private Long productId;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "설명")
    private String description;

    @Schema(description = "브랜드")
    private String brand;

    @Schema(description = "가격")
    private Integer price;

    @Schema(description = "총 재고")
    private Integer stock;

    @Schema(description = "카테고리 ID")
    private Long categoryId;

    @Schema(description = "메인 이미지 URL")
    private String imageUrl;

    @Schema(description = "추가 이미지")
    private List<String> additionalImages;

    @Schema(description = "상태")
    private ProductStatus status;

    @Schema(description = "생성 날짜")
    private Timestamp createdAt;

    @Schema(description = "사이즈별 재고")
    private List<ProductSizeDTO> productSizes;

    @Schema(description = "할인가")
    private Integer discountedPrice;

    @Schema(description = "주문 ID")
    private Integer orderId;

    public String getFormattedPrice() {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(price);
    }

    public String getFormattedDiscountedPrice() {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(discountedPrice);
    }

    public String getFormattedDiscountPercentage() {
        if (price != 0) {
            double discountPercentage = ((double)(price - discountedPrice) / price) * 100;
            DecimalFormat df = new DecimalFormat("#");
            return df.format(discountPercentage);
        }
        return "0";
    }

    public String getFormattedCreatedAt() {
        if (createdAt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(createdAt);
        }
        return "";
    }
}