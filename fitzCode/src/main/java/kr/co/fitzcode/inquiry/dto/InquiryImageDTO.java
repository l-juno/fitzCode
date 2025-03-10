package kr.co.fitzcode.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryImageDTO {
    private int imageId;
    private int inquiryId;
    private String imageUrl;            // 이미지 Url 또는 경로
    private int imageOrder;             // 이미지 순서
    private LocalDateTime createdAt;    // 이미지 생성날짜



}