package kr.co.fitzcode.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
    private int noticeId;
    private int userId;
    private String title;
    private String content;
    private String imageUrl;
    private String attachmentUrl;
    private String attachmentName;
    private Long attachmentSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}