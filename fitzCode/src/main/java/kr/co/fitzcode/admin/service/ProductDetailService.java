package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.ProductDetailDTO;
import kr.co.fitzcode.admin.dto.ProductSizeDTO;

import java.util.List;

public interface ProductDetailService {

    // 상품 상세 정보를 조회
    ProductDetailDTO getProductDetail(Long productId);

    // 상품 할인 가격을 업데이트
    void updateDiscountedPrice(Long productId, Integer discountedPrice);

    // 상품의 사이즈 목록 조회
    List<ProductSizeDTO> getSizesByProductId(Long productId);

    // 상품 사이즈 목록을 업데이트
    void updateSizes(Long productId, List<ProductSizeDTO> sizes);

    // 카테고리별 모든 사이즈 목록을 가져옴
    List<ProductSizeDTO> getAllSizes(boolean isShoeCategory, boolean isClothingCategory, List<ProductSizeDTO> existingSizes);

    // 상품 상태를 업데이트
    void updateStatus(Long productId, Integer status);
}