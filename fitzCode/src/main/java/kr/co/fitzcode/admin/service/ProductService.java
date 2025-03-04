package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.ProductDTO;
import java.util.List;

public interface ProductService {
    void addProduct(ProductDTO productDTO);
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long productId);
    void updateProduct(ProductDTO productDTO);
    void deleteProduct(Long productId);
}