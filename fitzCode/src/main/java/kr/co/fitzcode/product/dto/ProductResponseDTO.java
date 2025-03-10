package kr.co.fitzcode.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private static final int PRODUCT_PER_PAGE = 20;

    private List<ProductDTO> list;
    private int totalLength;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}
