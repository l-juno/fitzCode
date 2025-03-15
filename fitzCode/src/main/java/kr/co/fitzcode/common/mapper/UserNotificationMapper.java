package kr.co.fitzcode.common.mapper;

import kr.co.fitzcode.common.dto.NotificationDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserNotificationMapper {

    // 모든 사용자 ID 조회
    List<Integer> getAllUserIds();

    // 알림 삽입
    void insertNotification(NotificationDTO notificationDTO);

    // 사용자별 알림 조회
    List<NotificationDTO> getNotificationsByUserId(Integer userId);

    // 알림 삭제
    void deleteNotification(Integer notificationId);

    // 모든 알림 삭제
    void deleteAllNotifications(Integer userId);

    // 모든 알림 읽음 처리
    void markAllAsRead(Integer userId);
}