package kr.co.fitzcode.common.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Integer notificationId; // 알림 고유 ID
    private Integer userId; // 사용자 ID
    private Integer type; // 알림 유형
    private String message; // 알림 메시지
    private Boolean isRead; // 읽음 여부
    private LocalDateTime createdAt; // 알림 생성 시간
    private LocalDateTime readAt; // 알림 읽은 시간
    private Integer relatedId; // 관련된 공지사항 ID
}