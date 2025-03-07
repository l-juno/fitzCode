package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.ProductCategoryDTO;
import kr.co.fitzcode.admin.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    // 상품 추가
    void addProduct(ProductDTO productDTO);

    // 전체 상품 조회 (페이지네이션, 정렬, 검색 적용)
    List<ProductDTO> getAllProducts(int page, int pageSize, String sort, String keyword);

    // 특정 카테고리 상품 조회 (페이지네이션, 정렬, 검색 적용)
    List<ProductDTO> getProductsByCategory(Long categoryId, int page, int pageSize, String sort, String keyword);

    // 특정 상위 카테고리의 모든 하위 카테고리 상품 조회 (페이지네이션, 정렬, 검색 적용)
    List<ProductDTO> getProductsByParentCategory(Long parentCategoryId, int page, int pageSize, String sort, String keyword);

    // 전체 상품 개수 조회 (검색 적용)
    int countAllProducts(String keyword);

    // 특정 카테고리 상품 개수 조회 (검색 적용)
    int countProductsByCategory(Long categoryId, String keyword);

    // 특정 상위 카테고리 상품 개수 조회 (검색 적용)
    int countProductsByParentCategory(Long parentCategoryId, String keyword);

    // 상위 카테고리 목록 조회
    List<ProductCategoryDTO> getParentCategories();

    // 특정 상위 카테고리의 하위 카테고리 목록 조회
    List<ProductCategoryDTO> getChildCategories(Long parentCategoryId);
}