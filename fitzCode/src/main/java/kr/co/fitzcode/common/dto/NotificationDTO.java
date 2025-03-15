package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.common.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long notificationId;
    private Long userId;
    private NotificationType type;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private Long relatedId;
}