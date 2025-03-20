package kr.co.fitzcode.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.fitzcode.common.dto.ReviewDTO;
import kr.co.fitzcode.common.dto.AuthenticatedUser;
import kr.co.fitzcode.product.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ProductReviewController {

    private final ReviewService reviewService;

    public ProductReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "리뷰 조회", description = "특정 상품의 리뷰 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getReviews(
            @Parameter(description = "상품 ID", required = true) @RequestParam("productId") Long productId,
            @Parameter(description = "필터링 옵션 (all, photo, high-rating, low-rating)", required = false) @RequestParam(value = "filter", defaultValue = "all") String filter) {
        List<ReviewDTO> reviews = reviewService.getReviews(productId, filter);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "리뷰 작성", description = "새로운 리뷰를 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 작성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @Parameter(description = "상품 ID", required = true) @RequestParam("productId") Long productId,
            @Parameter(description = "평점 (1~5)", required = true) @RequestParam("rating") Integer rating,
            @Parameter(description = "리뷰 내용", required = true) @RequestParam("content") String content,
            @Parameter(description = "리뷰 이미지 파일", required = false) @RequestParam(value = "image", required = false) MultipartFile image) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body(null); // 인증되지 않은 사용자
        }

        // AuthenticatedUser 인터페이스로 처리
        AuthenticatedUser userDetails = (AuthenticatedUser) authentication.getPrincipal();
        Long userId = (long) userDetails.getUserId();

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setProductId(productId);
        reviewDTO.setRating(rating);
        reviewDTO.setContent(content);
        reviewDTO.setUserId(userId);

        ReviewDTO createdReview = reviewService.createReview(reviewDTO, image);
        return ResponseEntity.ok(createdReview);
    }
}