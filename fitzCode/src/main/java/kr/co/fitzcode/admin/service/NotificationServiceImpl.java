package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.NotificationMapper;
import kr.co.fitzcode.common.dto.NotificationDTO;
import kr.co.fitzcode.common.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationMapper notificationMapper;

    private void saveAndSendNotification(NotificationDTO notification) {
        // 데이터베이스에 알림 저장
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notificationMapper.insertNotification(notification);

        // WebSocket으로 알림 전송
        String destination = "/queue/notifications/" + notification.getUserId();
        messagingTemplate.convertAndSend(destination, notification);
    }

    @Override
    public void notifyNoticeCreated(Long userId, String message, Long noticeId) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(userId);
        notification.setType(NotificationType.NOTICE);
        notification.setMessage(message);
        notification.setRelatedId(noticeId);
        saveAndSendNotification(notification);
    }

    @Override
    public void notifyShippingUpdated(Long userId, String message, Long deliveryId) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(userId);
        notification.setType(NotificationType.SHIPPING);
        notification.setMessage(message);
        notification.setRelatedId(deliveryId);
        saveAndSendNotification(notification);
    }

    @Override
    public void notifyRefundCompleted(Long userId, String message, Long refundId) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(userId);
        notification.setType(NotificationType.REFUND);
        notification.setMessage(message);
        notification.setRelatedId(refundId);
        saveAndSendNotification(notification);
    }

    @Override
    public void notifyInquiryResponse(Long userId, String message, Long inquiryId) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(userId);
        notification.setType(NotificationType.INQUIRY_RESPONSE);
        notification.setMessage(message);
        notification.setRelatedId(inquiryId);
        saveAndSendNotification(notification);
    }

    @Override
    public void notifyAdminInquiryCreated(String message, Long inquiryId) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(0L); // 관리자 알림은 userId 0으로 설정
        notification.setType(NotificationType.INQUIRY_CREATED);
        notification.setMessage(message);
        notification.setRelatedId(inquiryId);
        saveAndSendNotification(notification);
    }

    @Override
    public void notifyAdminRefundRequest(String message, Long refundId) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(0L);
        notification.setType(NotificationType.REFUND_REQUEST);
        notification.setMessage(message);
        notification.setRelatedId(refundId);
        saveAndSendNotification(notification);
    }

    @Override
    public void notifyAdminPaymentCompleted(String message, Long paymentId) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(0L);
        notification.setType(NotificationType.PAYMENT);
        notification.setMessage(message);
        notification.setRelatedId(paymentId);
        saveAndSendNotification(notification);
    }

    @Override
    public void notifyAdminQnaCreated(String message, Long qnaId) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(0L);
        notification.setType(NotificationType.QNA_CREATED);
        notification.setMessage(message);
        notification.setRelatedId(qnaId);
        saveAndSendNotification(notification);
    }

    @Override
    public void notifyAdminReviewCreated(String message, Long reviewId) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(0L);
        notification.setType(NotificationType.REVIEW_CREATED);
        notification.setMessage(message);
        notification.setRelatedId(reviewId);
        saveAndSendNotification(notification);
    }

    @Override
    public List<NotificationDTO> getNotifications(Long userId) {
        return notificationMapper.findByUserId(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationMapper.markAsRead(notificationId);
    }
}