package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.common.dto.NotificationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
    void insertNotification(NotificationDTO notification);
    List<NotificationDTO> findByUserId(@Param("userId") Long userId);
    void markAsRead(@Param("notificationId") Long notificationId);
}