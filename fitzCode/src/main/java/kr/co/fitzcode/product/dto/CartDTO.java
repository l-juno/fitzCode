package kr.co.fitzcode.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private int cartId;
    private int userId;
    private int productId;
    private int quantity;
    private LocalDateTime createdAt;
    private int productSizeId;
}
