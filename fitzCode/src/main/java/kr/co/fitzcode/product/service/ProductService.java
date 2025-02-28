package kr.co.fitzcode.product.service;


import kr.co.fitzcode.product.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getProductsByPage(int pageNum);
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(int productId);
    void insertProduct(ProductDTO productDTO);
    void insertManyProducts(List<ProductDTO> productDTOList);
    void updateProduct(ProductDTO productDTO);
    void updateProductById(int productId);
    void deleteProductById(int productId);
}
