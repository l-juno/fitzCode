package kr.co.fitzcode.product.mapper;

import kr.co.fitzcode.common.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {
    List<ReviewDTO> findByProductId(@Param("productId") Long productId);
    List<ReviewDTO> findByProductIdWithImage(@Param("productId") Long productId);
    List<ReviewDTO> findByProductIdOrderByRatingDesc(@Param("productId") Long productId);
    List<ReviewDTO> findByProductIdOrderByRatingAsc(@Param("productId") Long productId);
    ReviewDTO findById(@Param("id") Long id);
    void insertReview(ReviewDTO reviewDTO);
    void insertReviewImage(@Param("reviewId") Long reviewId, @Param("imageUrl") String imageUrl);
}