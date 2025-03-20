package kr.co.fitzcode.product.service;

import kr.co.fitzcode.common.dto.ReviewDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getReviews(Long productId, String filter);
    ReviewDTO createReview(ReviewDTO reviewDTO, MultipartFile image);
}