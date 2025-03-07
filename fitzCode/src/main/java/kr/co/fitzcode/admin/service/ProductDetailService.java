package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.ProductDetailDTO;
import kr.co.fitzcode.admin.dto.ProductSizeDTO;
import kr.co.fitzcode.admin.dto.QnaDTO;
import kr.co.fitzcode.admin.dto.ReviewDTO;

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

    // 상품 삭제
    void deleteProduct(Long productId);

    // 특정 상품의 리뷰 목록 조회
    List<ReviewDTO> getReviewsByProductId(Long productId);

    // 리뷰 삭제
    void deleteReview(Long reviewId);

    // 특정 상품의 Q&A 목록 조회
    List<QnaDTO> getQnasByProductId(Long productId);

    // Q&A 답변 추가
    void addQnaAnswer(Long qnaId, String answer);

    // Q&A 답변 수정
    void updateQnaAnswer(Long qnaId, String answer);

    // Q&A 삭제
    void deleteQna(Long qnaId);
}