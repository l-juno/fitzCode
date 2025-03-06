package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.admin.dto.ProductDetailDTO;
import kr.co.fitzcode.admin.dto.ProductImageDTO;
import kr.co.fitzcode.admin.dto.ProductSizeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductDetailMapper {

    // 상품 기본 정보 조회
    ProductDetailDTO findProductDetailById(@Param("productId") Long productId);

    // 상품 이미지 목록 조회
    List<ProductImageDTO> findProductImagesById(@Param("productId") Long productId);

    // 상품 사이즈 목록 조회
    List<ProductSizeDTO> findSizesByProductId(@Param("productId") Long productId);

    // 사이즈 재고 수정
    int updateProductSizeStock(ProductSizeDTO productSizeDTO);

    // 할인 가격 수정
    int updateDiscountedPrice(@Param("productId") Long productId, @Param("discountedPrice") Integer discountedPrice);

    // 새로운 사이즈 데이터 인서트
    int insertProductSize(ProductSizeDTO productSizeDTO);
}