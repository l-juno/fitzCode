package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.admin.dto.ProductCategoryDTO;
import kr.co.fitzcode.admin.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    // 상품 등록
    void insertProduct(ProductDTO productDTO);

    // 전체 상품 조회 (페이지네이션, 정렬, 검색 적용)
    List<ProductDTO> getAllProducts(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("sort") String sort,
            @Param("keyword") String keyword);

    // 특정 카테고리 상품 조회 (페이지네이션, 정렬, 검색 적용)
    List<ProductDTO> getProductsByCategory(
            @Param("categoryId") Long categoryId,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("sort") String sort,
            @Param("keyword") String keyword);

    // 특정 상위 카테고리의 모든 하위 카테고리 상품 조회 (페이지네이션, 정렬, 검색 적용)
    List<ProductDTO> getProductsByParentCategory(
            @Param("parentCategoryId") Long parentCategoryId,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("sort") String sort,
            @Param("keyword") String keyword);

    // 특정 상품 조회
    ProductDTO getProductById(@Param("productId") Long productId);

    // 상품 수정
    void updateProduct(ProductDTO productDTO);

    // 상품 삭제
    void deleteProduct(@Param("productId") Long productId);

    // 전체 상품 개수 조회 (검색 적용)
    int countAllProducts(@Param("keyword") String keyword);

    // 특정 카테고리 상품 개수 조회 (검색 적용)
    int countProductsByCategory(
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword);

    // 특정 상위 카테고리의 모든 하위 카테고리 상품 개수 조회 (검색 적용)
    int countProductsByParentCategory(
            @Param("parentCategoryId") Long parentCategoryId,
            @Param("keyword") String keyword);

    // 상위 카테고리 목록 조회
    List<ProductCategoryDTO> getParentCategories();

    // 특정 상위 카테고리의 하위 카테고리 목록 조회
    List<ProductCategoryDTO> getChildCategories(@Param("parentId") Long parentId);
}