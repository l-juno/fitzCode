package kr.co.fitzcode.mypage.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    int productId;
    String productName;
    String productDesc;
    String brand;
    int price;
    int stock;
    int categoryId;
    String imageUrl;
    LocalDateTime createdAt;

}
