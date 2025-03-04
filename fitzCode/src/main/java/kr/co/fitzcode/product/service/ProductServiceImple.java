package kr.co.fitzcode.product.service;

import kr.co.fitzcode.product.dto.ProductDTO;
import kr.co.fitzcode.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImple implements ProductService {

    private final ProductMapper productMapper;
    private final int PRODUCT_PER_PAGE = 20;

    @Override
    public List<ProductDTO> getProductsByPage(int pageNum) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("offset", (pageNum-1) * PRODUCT_PER_PAGE);
        params.put("limit", PRODUCT_PER_PAGE);
        return productMapper.getProductsByPage(params);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productMapper.getAllProducts();
    }

    @Override
    public ProductDTO getProductById(int productId) {
        return productMapper.getProductById(productId);
    }

    @Override
    public void insertProduct(ProductDTO productDTO) {
        productMapper.insertProduct(productDTO);
    }

    @Override
    public void insertManyProducts(List<ProductDTO> productDTOList) {
        productMapper.insertManyProducts(productDTOList);
    }

    @Override
    public void updateProduct(ProductDTO productDTO) {
        productMapper.updateProduct(productDTO);
    }

    @Override
    public void updateProductById(int productId) {
        ProductDTO productDTO = productMapper.getProductById(productId);
        this.updateProduct(productDTO);
    }

    @Override
    public void deleteProductById(int productId) {
        productMapper.deleteProductById(productId);
    }

    @Override
    public int getNumberOfPages() {
        int products = productMapper.getNumberOfProducts();
        return (products/PRODUCT_PER_PAGE);
    }

    @Override
    public List<ProductDTO> getProductsByFilter(Map<String, String> filters) {
        return productMapper.getProductsByFilter(filters);
    }
}
