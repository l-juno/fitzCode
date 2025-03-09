package kr.co.fitzcode.admin.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ReviewDTO {
    private Long reviewId;
    private Long productId;
    private Long userId;
    private String userName;
    private Integer rating;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<String> imageUrls;
}