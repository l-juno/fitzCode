package kr.co.fitzcode.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.fitzcode.common.dto.CustomOAuth2User;
import kr.co.fitzcode.common.dto.InquiryDTO;
import kr.co.fitzcode.common.dto.NoticeDTO;
import kr.co.fitzcode.common.dto.NotificationDTO;
import kr.co.fitzcode.common.enums.NotificationType;
import kr.co.fitzcode.common.mapper.UserNotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {

    private final UserNotificationMapper notificationMapper;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>(); // 사용자별 SSE 연결

    // 사용자가 알림 구독을 시작하도록 SSE 연결을 설정
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

    // 모든 사용자에게 공지사항 알림을 전송하고 DB에 저장
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

            saveNotificationsForAllUsers(noticeDTO);
        } catch (JsonProcessingException e) {
            log.error("JSON 직렬화 오류: {}", e.getMessage(), e);
        }
    }

    // 특정 사용자에게 1대1 문의 답변 알림을 전송
    public void sendNotificationToUser(String eventName, InquiryDTO inquiryDTO, int userId) {
        log.info("특정 사용자에게 알림 전송 시작 - 이벤트: {}, 사용자 ID: {}, 문의 제목: {}", eventName, userId, inquiryDTO.getSubject());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String jsonData = objectMapper.writeValueAsString(inquiryDTO);
            log.info("JSON: {}", jsonData);

            String targetUserId = String.valueOf(userId);
            SseEmitter emitter = emitters.get(targetUserId);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event().name(eventName).data(jsonData));
                    log.info("이벤트 전송 성공, userId: {}, 이벤트: {}", targetUserId, eventName);
                } catch (IOException e) {
                    log.error("이벤트 전송 실패, userId: {}, 오류: {}", targetUserId, e.getMessage());
                    emitters.remove(targetUserId);
                }
            } else {
                log.warn("연결된 클라이언트 없음, userId: {}", targetUserId);
            }

            saveNotificationForUser(inquiryDTO, userId);
            log.info("알림 DB 저장 완료, userId: {}, relatedId: {}", userId, inquiryDTO.getInquiryId());
        } catch (JsonProcessingException e) {
            log.error("JSON 직렬화 오류: {}", e.getMessage(), e);
        }
    }

    // 특정 사용자에게 1대1 문의 답변 알림을 DB에 저장
    private void saveNotificationForUser(InquiryDTO inquiryDTO, int userId) {
        log.info("사용자 ID: {}에 알림 저장 시작, 문의 ID: {}", userId, inquiryDTO.getInquiryId());
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(userId);
        notification.setType(NotificationType.INQUIRY_RESPONSE.getCode());
        notification.setMessage("1대1 문의 답변이 있습니다");
        notification.setRelatedId(Integer.valueOf(inquiryDTO.getInquiryId()));
        notificationMapper.insertNotification(notification);
        log.info("사용자 ID: {}에 대한 알림 저장 완료, notificationId: {}", userId, notification.getNotificationId());
    }

    // 모든 사용자에게 공지사항 알림을 DB에 저장
    private void saveNotificationsForAllUsers(NoticeDTO noticeDTO) {
        log.info("모든 사용자에게 알림 저장 시작, 공지사항 ID: {}", noticeDTO.getNoticeId());
        List<Integer> userIds = notificationMapper.getAllUserIds();
        log.info("조회된 사용자 ID 수: {}", userIds.size());
        for (Integer userId : userIds) {
            NotificationDTO notification = new NotificationDTO();
            notification.setUserId(userId);
            notification.setType(NotificationType.NOTICE.getCode());
            notification.setMessage("새 공지사항: " + noticeDTO.getTitle());
            notification.setRelatedId(Integer.valueOf(noticeDTO.getNoticeId())); // int를 Integer로 변환
            notificationMapper.insertNotification(notification);
            log.info("사용자 ID: {}에 대한 알림 저장 완료, notificationId: {}", userId, notification.getNotificationId());
        }
    }

    // 새로운 알림을 DB에 저장
    public void createNotification(NotificationDTO notificationDTO) {
        notificationMapper.insertNotification(notificationDTO);
        log.info("알림 생성 완료, notificationId: {}", notificationDTO.getNotificationId());
    }

    // 사용자 ID를 기준으로 알림 목록을 조회
    public List<NotificationDTO> getNotificationsByUserId(Integer userId) {
        return notificationMapper.getNotificationsByUserId(userId);
    }

    // 특정 알림을 삭제
    public void deleteNotification(Integer notificationId) {
        notificationMapper.deleteNotification(notificationId);
    }

    // 사용자의 모든 알림을 삭제
    public void deleteAllNotifications(Integer userId) {
        notificationMapper.deleteAllNotifications(userId);
    }

    // 사용자의 모든 알림을 읽음 처리
    public void markAllAsRead(Integer userId) {
        notificationMapper.markAllAsRead(userId);
    }

    // Principal에서 사용자 ID 가져옴
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