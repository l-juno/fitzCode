package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.*;
import kr.co.fitzcode.admin.mapper.ProductDetailMapper;
import kr.co.fitzcode.common.enums.ProductSize;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {

    private static final Logger log = LoggerFactory.getLogger(ProductDetailServiceImpl.class);
    private final ProductDetailMapper productDetailMapper;

    @Override
    public ProductDetailDTO getProductDetail(Long productId) {
        ProductDetailDTO product = productDetailMapper.findProductDetailById(productId);
        if (product == null) {
            return null;
        }

        List<ProductImageDTO> images = productDetailMapper.findProductImagesById(productId);
        log.info("이미지 조회됨: {}", images != null ? images.size() : "없음");
        if (images != null) {
            for (ProductImageDTO img : images) {
                if (img.getImageUrl() == null || img.getImageUrl().isEmpty()) {
                    img.setImageUrl("/images/fallback.jpg");
                }
            }
        }
        product.setImages(images != null ? images : new ArrayList<>());

        List<ProductSizeDTO> sizes = productDetailMapper.findSizesByProductId(productId);
        if (sizes != null) {
            for (ProductSizeDTO size : sizes) {
                size.getSizeDescription();
            }
        }
        product.setSizes(sizes != null ? sizes : new ArrayList<>());

        // 카테고리별 사이즈 필터링
        Integer categoryId = product.getCategoryId();
        boolean isShoeCategory = isShoeCategory(categoryId);
        boolean isClothingCategory = isClothingCategory(categoryId);

        List<ProductSizeDTO> allSizes = getAllSizes(isShoeCategory, isClothingCategory, product.getSizes());
        product.setAllSizes(allSizes);
        product.setSizes(new ArrayList<>(allSizes)); // sizes를 allSizes로 초기화해줌

        return product;
    }

    // 신발 카테고리 여부 확인
    private boolean isShoeCategory(Integer categoryId) {
        if (categoryId == null) return false;
        // 상위 카테고리 (category_id = 1) 및 하위 카테고리 (4, 5, 6, 10, 11, 12)
        return categoryId == 1 || categoryId == 4 || categoryId == 5 || categoryId == 6 ||
                categoryId == 10 || categoryId == 11 || categoryId == 12;
    }

    // 의류 카테고리 여부 확인
    private boolean isClothingCategory(Integer categoryId) {
        if (categoryId == null) return false;
        // 상의 (category_id = 2) 및 하위 카테고리 (7, 8, 9)
        // 하의 (category_id = 3) 및 하위 카테고리 (14, 15, 16)
        return (categoryId == 2 || categoryId == 7 || categoryId == 8 || categoryId == 9) ||
                (categoryId == 3 || categoryId == 14 || categoryId == 15 || categoryId == 16);
    }

    @Override
    public List<ProductSizeDTO> getAllSizes(boolean isShoeCategory, boolean isClothingCategory, List<ProductSizeDTO> existingSizes) {
        List<ProductSizeDTO> allSizes = new ArrayList<>();
        for (ProductSize size : ProductSize.values()) {
            // 신발 카테고리: 신발 사이즈(1~9)만 포함
            if (isShoeCategory && size.getCode() <= 9) {
                ProductSizeDTO dto = new ProductSizeDTO();
                dto.setSizeCode(size.getCode());
                // 기존 사이즈 데이터가 있으면 재고 반영
                if (existingSizes != null) {
                    for (ProductSizeDTO existing : existingSizes) {
                        if (existing.getSizeCode() != null && existing.getSizeCode().equals(size.getCode())) {
                            dto.setProductSizeId(existing.getProductSizeId());
                            dto.setStock(existing.getStock() != null ? existing.getStock() : 0);
                            break;
                        }
                    }
                }
                if (dto.getStock() == null) dto.setStock(0); // 데이터 없으면 0
                allSizes.add(dto);
            }
            // 의류 카테고리: 의류 사이즈(10~15)만 포함
            else if (isClothingCategory && size.getCode() >= 10) {
                ProductSizeDTO dto = new ProductSizeDTO();
                dto.setSizeCode(size.getCode());
                // 기존 사이즈 데이터가 있으면 재고 반영
                if (existingSizes != null) {
                    for (ProductSizeDTO existing : existingSizes) {
                        if (existing.getSizeCode() != null && existing.getSizeCode().equals(size.getCode())) {
                            dto.setProductSizeId(existing.getProductSizeId());
                            dto.setStock(existing.getStock() != null ? existing.getStock() : 0);
                            break;
                        }
                    }
                }
                if (dto.getStock() == null) dto.setStock(0); // 데이터 없으면 0
                allSizes.add(dto);
            }
        }
        log.info("신발 카테고리 : {}, 의류 카테고리 : {}, 전체 사이즈 개수: {}", isShoeCategory, isClothingCategory, allSizes.size());
        return allSizes;
    }

    @Override
    public void updateDiscountedPrice(Long productId, Integer discountedPrice) {
        if (discountedPrice == null) {
            discountedPrice = 0;
        }
        productDetailMapper.updateDiscountedPrice(productId, discountedPrice);
    }

    @Override
    public List<ProductSizeDTO> getSizesByProductId(Long productId) {
        return productDetailMapper.findSizesByProductId(productId);
    }

    @Override
    public void updateSizes(Long productId, List<ProductSizeDTO> sizes) {
        if (sizes != null) {
            for (ProductSizeDTO dto : sizes) {
                dto.setProductId(productId);
                log.info("사이즈 : 상품 사이즈 ID={}, 사이즈 코드={}, 재고={}",
                        dto.getProductSizeId(), dto.getSizeCode(), dto.getStock());
                if (dto.getSizeCode() == null) {
                    log.error("사이즈 코드가 null 상품 ID={}, DTO={}", productId, dto);
                    throw new IllegalArgumentException("사이즈코드는 null이면 안됨");
                }
                if (dto.getProductSizeId() == null) {
                    productDetailMapper.insertProductSize(dto);
                } else {
                    productDetailMapper.updateProductSizeStock(dto);
                }
            }
        }
    }

    @Override
    public void updateStatus(Long productId, Integer status) {
        log.info("상품 ID={}의 상태를 {}로 업데이트", productId, status);
        if (status == null || (status != 1 && status != 2 && status != 3)) {
            throw new IllegalArgumentException("유효하지 않은 상태 값: " + status);
        }
        productDetailMapper.updateProductStatus(productId, status);
    }

    @Override
    public void deleteProduct(Long productId) {
        ProductDetailDTO product = productDetailMapper.findProductDetailById(productId);
        if (product == null) {
            throw new IllegalArgumentException("삭제하려는 상품이 존재하지 않습니다. ID: " + productId);
        }
        productDetailMapper.deleteProduct(productId);
        log.info("상품 ID={}가 삭제됨", productId);
    }

    @Override
    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        List<ReviewDTO> reviews = productDetailMapper.findReviewsByProductId(productId);
        for (ReviewDTO review : reviews) {
            List<String> imageUrls = productDetailMapper.findReviewImagesByReviewId(review.getReviewId());
            review.setImageUrls(imageUrls != null ? imageUrls : new ArrayList<>());
        }
        return reviews;
    }

    @Override
    public void deleteReview(Long reviewId) {
        ReviewDTO review = productDetailMapper.findReviewById(reviewId);
        if (review == null) {
            throw new IllegalArgumentException("삭제하려는 리뷰가 존재하지 않습니다. ID: " + reviewId);
        }
        productDetailMapper.deleteReview(reviewId);
        log.info("리뷰 ID={}가 삭제됨", reviewId);
    }

    @Override
    public List<QnaDTO> getQnasByProductId(Long productId) {
        return productDetailMapper.findQnasByProductId(productId);
    }

    @Override
    public void addQnaAnswer(Long qnaId, String answer) {
        if (qnaId == null || answer == null || answer.trim().isEmpty()) {
            throw new IllegalArgumentException("Q&A ID 또는 답변이 유효하지 않습니다.");
        }
        productDetailMapper.updateQnaAnswer(qnaId, answer);
        log.info("Q&A ID={}에 답변 추가됨: {}", qnaId, answer);
    }

    @Override
    public void updateQnaAnswer(Long qnaId, String answer) {
        if (qnaId == null || answer == null || answer.trim().isEmpty()) {
            throw new IllegalArgumentException("Q&A ID 또는 수정할 답변이 유효하지 않습니다.");
        }
        productDetailMapper.updateQnaAnswer(qnaId, answer);
        log.info("Q&A ID={}의 답변 수정됨: {}", qnaId, answer);
    }

    @Override
    public void deleteQna(Long qnaId) {
        QnaDTO qna = productDetailMapper.findQnaById(qnaId);
        if (qna == null) {
            throw new IllegalArgumentException("삭제하려는 Q&A가 존재하지 않습니다. ID: " + qnaId);
        }
        productDetailMapper.deleteQna(qnaId);
        log.info("Q&A ID={}가 삭제됨", qnaId);
    }
}