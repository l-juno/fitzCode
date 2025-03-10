package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.common.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductDetailDTO {
    private Long productId;
    private String productName;
    private String description;
    private String brand;
    private int price;
    private int stock;
    private int status;
    private String categoryName;
    private String imageUrl;
    private Integer categoryId;
    private Integer discountedPrice;

    private List<ProductSizeDTO> sizes = new ArrayList<>();
    private List<ProductImageDTO> images = new ArrayList<>();
    private List<ProductSizeDTO> allSizes = new ArrayList<>();

    public String getStatusDesc() {
        return ProductStatus.fromCode(this.status).getDescription();
    }

    public Integer getDiscountRate() {
        if (discountedPrice == null || discountedPrice <= 0) {
            return 0;
        }
        if (price <= 0) {
            return 0;
        }
        int diff = price - discountedPrice;
        double rate = (diff / (double) price) * 100.0;
        return (int) Math.round(rate);
    }
}