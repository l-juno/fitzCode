package kr.co.fitzcode.product.service;

import kr.co.fitzcode.common.dto.ReviewDTO;
import kr.co.fitzcode.product.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public ReviewServiceImpl(ReviewMapper reviewMapper, S3Client s3Client) {
        this.reviewMapper = reviewMapper;
        this.s3Client = s3Client;
    }

    @Override
    public List<ReviewDTO> getReviews(Long productId, String filter) {
        List<ReviewDTO> reviews;
        switch (filter) {
            case "photo":
                reviews = reviewMapper.findByProductIdWithImage(productId);
                break;
            case "high-rating":
                reviews = reviewMapper.findByProductIdOrderByRatingDesc(productId);
                break;
            case "low-rating":
                reviews = reviewMapper.findByProductIdOrderByRatingAsc(productId);
                break;
            case "all":
            default:
                reviews = reviewMapper.findByProductId(productId);
                break;
        }

        // 날짜 포맷팅
        SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd");
        reviews.forEach(review -> {
            if (review.getCreatedAt() != null) {
                review.setCreatedAtStr(formatter.format(review.getCreatedAt()));
            }
        });

        return reviews;
    }

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO, MultipartFile image) {
        // 리뷰 저장
        reviewMapper.insertReview(reviewDTO);

        // 이미지 업로드 및 저장
        if (image != null && !image.isEmpty()) {
            try {
                String fileName = "reviews/" + UUID.randomUUID() + "_" + image.getOriginalFilename();
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build();
                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));

                String imageUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toString();
                List<String> imageUrls = new ArrayList<>();
                imageUrls.add(imageUrl);
                reviewDTO.setImageUrls(imageUrls);

                // 리뷰 이미지 저장
                reviewMapper.insertReviewImage(reviewDTO.getReviewId(), imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image to S3", e);
            }
        }

        // 저장된 리뷰 조회
        ReviewDTO savedReview = reviewMapper.findById(reviewDTO.getReviewId());
        if (savedReview.getCreatedAt() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd");
            savedReview.setCreatedAtStr(formatter.format(savedReview.getCreatedAt()));
        }

        return savedReview;
    }
}