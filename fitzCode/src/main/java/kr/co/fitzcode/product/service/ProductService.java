package kr.co.fitzcode.product.service;


import kr.co.fitzcode.product.dto.ProductDTO;
import kr.co.fitzcode.product.dto.ProductImageDTO;
import kr.co.fitzcode.product.dto.ProductSizeDTO;

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

    List<ProductDTO> getProductsByFilterAndPage(List<String> filters, String searchText, int currentPage);
    int getProductsCountByFilter(List<String> filters, String searchText);
    int getCountOfAllProducts();

    List<ProductImageDTO> getProductImagesByProductId(int productId);

    List<ProductSizeDTO> getAllSizeOfProduct(int productId);
}
