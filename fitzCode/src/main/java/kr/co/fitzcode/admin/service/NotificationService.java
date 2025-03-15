package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.common.dto.NotificationDTO;
import java.util.List;

public interface NotificationService {

    void notifyNoticeCreated(Long userId, String message, Long noticeId);

    void notifyShippingUpdated(Long userId, String message, Long deliveryId);

    void notifyRefundCompleted(Long userId, String message, Long refundId);

    void notifyInquiryResponse(Long userId, String message, Long inquiryId);

    void notifyAdminInquiryCreated(String message, Long inquiryId);

    void notifyAdminRefundRequest(String message, Long refundId);

    void notifyAdminPaymentCompleted(String message, Long paymentId);

    void notifyAdminQnaCreated(String message, Long qnaId);

    void notifyAdminReviewCreated(String message, Long reviewId);

    List<NotificationDTO> getNotifications(Long userId);

    void markAsRead(Long notificationId);
}