package kr.co.fitzcode.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.fitzcode.common.dto.CustomOAuth2User;
import kr.co.fitzcode.common.dto.NoticeDTO;
import kr.co.fitzcode.common.dto.NotificationDTO;
import kr.co.fitzcode.common.enums.NotificationType;
import kr.co.fitzcode.common.mapper.UserNotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final UserNotificationMapper notificationMapper;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>(); // 사용자별 SSE 연결

    public SseEmitter subscribe(Principal principal) {
        String userId = principal != null ? String.valueOf(extractUserId(principal)) : "anonymous-" + System.currentTimeMillis();
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1시간 타임아웃

        emitter.onCompletion(() -> {
            log.info("SSE 연결 종료, userId: {}, 남은 클라이언트 수: {}", userId, emitters.size());
            emitters.remove(userId);
        });
        emitter.onTimeout(() -> {
            log.info("SSE 연결 타임아웃, userId: {}, 남은 클라이언트 수: {}", userId, emitters.size());
            emitters.remove(userId);
        });
        emitter.onError((e) -> {
            log.error("SSE 연결 오류 발생, userId: {}, 오류: {}, 남은 클라이언트 수: {}", userId, e.getMessage(), emitters.size());
            emitters.remove(userId);
        });

        emitters.put(userId, emitter);
        log.info("새로운 클라이언트 구독 완료, userId: {}, 현재 연결된 클라이언트 수: {}", userId, emitters.size());

        try {
            emitter.send(SseEmitter.event().name("INIT").data("Connected successfully"));
            log.info("INIT 이벤트 전송 완료, userId: {}", userId);
        } catch (IOException e) {
            log.error("INIT 이벤트 전송 실패, userId: {}, 오류: {}", userId, e.getMessage());
            emitters.remove(userId);
        }

        return emitter;
    }

    public void sendNotificationToAll(String eventName, NoticeDTO noticeDTO) {
        log.info("모든 클라이언트에게 알림 전송 시작 - 이벤트: {}, 공지사항 제목: {}", eventName, noticeDTO.getTitle());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String jsonData = objectMapper.writeValueAsString(noticeDTO);
            log.info("JSON 데이터 준비: {}", jsonData);

            List<String> deadEmitters = new ArrayList<>();
            for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
                String userId = entry.getKey();
                SseEmitter emitter = entry.getValue();
                try {
                    emitter.send(SseEmitter.event().name(eventName).data(jsonData));
                    log.info("이벤트 전송 성공, userId: {}, 이벤트: {}", userId, eventName);
                } catch (IOException e) {
                    log.error("이벤트 전송 실패, userId: {}, 오류: {}", userId, e.getMessage());
                    deadEmitters.add(userId);
                }
            }

            deadEmitters.forEach(emitters::remove);
            log.info("연결 정리 후 클라이언트 수: {}", emitters.size());

            // 모든 사용자에게 알림 저장
            saveNotificationsForAllUsers(noticeDTO);
        } catch (JsonProcessingException e) {
            log.error("JSON 직렬화 오류: {}", e.getMessage(), e);
        }
    }

    private void saveNotificationsForAllUsers(NoticeDTO noticeDTO) {
        log.info("모든 사용자에게 알림 저장 시작, 공지사항 ID: {}", noticeDTO.getNoticeId());
        List<Integer> userIds = notificationMapper.getAllUserIds();
        log.info("조회된 사용자 ID 수: {}", userIds.size());
        for (Integer userId : userIds) {
            NotificationDTO notification = new NotificationDTO();
            notification.setUserId(userId);
            notification.setType(NotificationType.NOTICE.getCode());
            notification.setMessage("새 공지사항: " + noticeDTO.getTitle());
            notification.setRelatedId(noticeDTO.getNoticeId());
            notificationMapper.insertNotification(notification);
            log.info("사용자 ID: {}에 대한 알림 저장 완료", userId);
        }
    }

    public void createNotification(NotificationDTO notificationDTO) {
        notificationMapper.insertNotification(notificationDTO);
        log.info("알림 생성 완료, notificationId: {}", notificationDTO.getNotificationId());
    }

    public List<NotificationDTO> getNotificationsByUserId(Integer userId) {
        return notificationMapper.getNotificationsByUserId(userId);
    }

    public void deleteNotification(Integer notificationId) {
        notificationMapper.deleteNotification(notificationId);
    }

    public void deleteAllNotifications(Integer userId) {
        notificationMapper.deleteAllNotifications(userId);
    }

    public void markAllAsRead(Integer userId) {
        notificationMapper.markAllAsRead(userId);
    }

    private int extractUserId(Principal principal) {
        Object principalObj = principal;

        if (principalObj instanceof UsernamePasswordAuthenticationToken) {
            principalObj = ((UsernamePasswordAuthenticationToken) principalObj).getPrincipal();
        } else if (principalObj instanceof OAuth2AuthenticationToken) {
            principalObj = ((OAuth2AuthenticationToken) principalObj).getPrincipal();
        }

        if (principalObj instanceof CustomUserDetails) {
            return ((CustomUserDetails) principalObj).getUserId();
        } else if (principalObj instanceof CustomOAuth2User) {
            return ((CustomOAuth2User) principalObj).getUserId();
        } else {
            log.error("알 수 없는 Principal 내부 타입: {}", principalObj.getClass().getName());
            throw new IllegalStateException("알 수 없는 Principal 내부 타입: " + principalObj.getClass().getName());
        }
    }
}