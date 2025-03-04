package kr.co.fitzcode.product.service;


import kr.co.fitzcode.product.dto.ProductDTO;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductDTO> getProductsByPage(int pageNum);
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(int productId);
    void insertProduct(ProductDTO productDTO);
    void insertManyProducts(List<ProductDTO> productDTOList);
    void updateProduct(ProductDTO productDTO);
    void updateProductById(int productId);
    void deleteProductById(int productId);

    int getNumberOfPages();

    List<ProductDTO> getProductsByFilter(Map<String, String> filters);
}
