package kr.co.fitzcode.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private int productId;
    private String productName;
    private String description;
    private String brand;
    private int price;
    private int discountedPrice;
    private int stock;
    private int categoryId;
    private String imageUrl;
    private LocalDateTime createdAt;

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

}
