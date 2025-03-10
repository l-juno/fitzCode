package kr.co.fitzcode.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int stock;
    private int categoryId;
    private String imageUrl;
    private LocalDateTime createdAt;
}
