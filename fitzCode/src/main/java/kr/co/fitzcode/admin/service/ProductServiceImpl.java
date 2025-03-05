package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.ProductCategoryDTO;
import kr.co.fitzcode.admin.dto.ProductDTO;
import kr.co.fitzcode.admin.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    // 상품 추가
    @Override
    @Transactional
    public void addProduct(ProductDTO productDTO) {
        productMapper.insertProduct(productDTO);
    }

    // 전체 상품 조회 (페이지네이션, 정렬, 검색 적용)
    @Override
    public List<ProductDTO> getAllProducts(int page, int pageSize, String sort, String keyword) {
        int offset = (page - 1) * pageSize;
        return productMapper.getAllProducts(offset, pageSize, sort, keyword);
    }

    // 특정 카테고리 상품 조회 (페이지네이션, 정렬, 검색 적용)
    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId, int page, int pageSize, String sort, String keyword) {
        int offset = (page - 1) * pageSize;
        return productMapper.getProductsByCategory(categoryId, offset, pageSize, sort, keyword);
    }

    // 특정 상위 카테고리의 모든 하위 카테고리 상품 조회 (페이지네이션, 정렬, 검색 적용)
    @Override
    public List<ProductDTO> getProductsByParentCategory(Long parentCategoryId, int page, int pageSize, String sort, String keyword) {
        int offset = (page - 1) * pageSize;
        return productMapper.getProductsByParentCategory(parentCategoryId, offset, pageSize, sort, keyword);
    }

    // 전체 상품 개수 조회 (검색 적용)
    @Override
    public int countAllProducts(String keyword) {
        return productMapper.countAllProducts(keyword);
    }

    // 특정 카테고리 상품 개수 조회 (검색 적용)
    @Override
    public int countProductsByCategory(Long categoryId, String keyword) {
        return productMapper.countProductsByCategory(categoryId, keyword);
    }

    // 특정 상위 카테고리 상품 개수 조회 (검색 적용)
    @Override
    public int countProductsByParentCategory(Long parentCategoryId, String keyword) {
        return productMapper.countProductsByParentCategory(parentCategoryId, keyword);
    }

    // 상위 카테고리 목록 조회
    @Override
    public List<ProductCategoryDTO> getParentCategories() {
        return productMapper.getParentCategories();
    }

    // 특정 상위 카테고리의 하위 카테고리 목록 조회
    @Override
    public List<ProductCategoryDTO> getChildCategories(Long parentId) {
        return productMapper.getChildCategories(parentId);
    }
}