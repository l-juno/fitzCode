package kr.co.fitzcode.admin.service.impl;

import kr.co.fitzcode.admin.dto.ProductDTO;
import kr.co.fitzcode.admin.mapper.ProductMapper;
import kr.co.fitzcode.admin.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    @Transactional
    public void addProduct(ProductDTO productDTO) {
        productMapper.insertProduct(productDTO);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productMapper.getAllProducts();
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        return productMapper.getProductById(productId);
    }

    @Override
    @Transactional
    public void updateProduct(ProductDTO productDTO) {
        productMapper.updateProduct(productDTO);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        productMapper.deleteProduct(productId);
    }
}