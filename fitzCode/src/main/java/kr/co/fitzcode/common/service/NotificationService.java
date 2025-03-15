package kr.co.fitzcode.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.fitzcode.common.dto.NoticeDTO;
import kr.co.fitzcode.common.dto.NotificationDTO;
import kr.co.fitzcode.common.enums.NotificationType;
import kr.co.fitzcode.common.mapper.UserNotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final UserNotificationMapper notificationMapper;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>(); // SSE 클라이언트 연결 리스트

    // 새로운 클라이언트가 알림 구독을 시작
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1시간 타임아웃

        // 연결 종료 시 처리
        emitter.onCompletion(() -> {
            log.info("SSE 연결 종료, 남은 클라이언트 수: {}", emitters.size());
            emitters.remove(emitter);
        });

        // 타임아웃 시 처리
        emitter.onTimeout(() -> {
            log.info("SSE 연결 타임아웃, 남은 클라이언트 수: {}", emitters.size());
            emitters.remove(emitter);
        });

        // 오류 발생 시 처리
        emitter.onError((e) -> {
            log.error("SSE 연결 오류 발생: {}, 남은 클라이언트 수: {}", e.getMessage(), emitters.size());
            emitters.remove(emitter);
        });

        this.emitters.add(emitter);
        log.info("새로운 클라이언트 구독 완료. 현재 연결된 클라이언트 수: {}", emitters.size());

        try {
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("Connected successfully"));
            log.info("클라이언트에게 INIT 이벤트 전송 완료");
        } catch (IOException e) {
            log.error("INIT 이벤트 전송 실패: {}", e.getMessage(), e);
            emitters.remove(emitter);
        }

        return emitter;
    }

    // 모든 클라이언트에게 알림을 전송
    public void sendNotificationToAll(String eventName, NoticeDTO noticeDTO) {
        log.info("모든 클라이언트에게 알림 전송 시작 - 이벤트: {}, 공지사항 제목: {}", eventName, noticeDTO.getTitle());
        List<SseEmitter> deadEmitters = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String jsonData = objectMapper.writeValueAsString(noticeDTO);
            log.info("{} 이벤트 전송 준비 - JSON 데이터: {}", eventName, jsonData);
            log.info("현재 연결된 클라이언트 수: {}", emitters.size());

            if (emitters.isEmpty()) {
                log.warn("연결된 클라이언트가 없습니다. 알림 전송 취소");
                return;
            }

            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name(eventName)
                            .data(jsonData));
                    log.info("{} 이벤트 전송 성공 - 클라이언트: {}", eventName, emitter);
                } catch (IOException e) {
                    log.error("{} 이벤트 전송 실패 - 클라이언트: {}, 오류: {}", eventName, emitter, e.getMessage());
                    deadEmitters.add(emitter);
                }
            }

            // 모든 사용자에게 알림 저장
            saveNotificationsForAllUsers(noticeDTO);
        } catch (JsonProcessingException e) {
            log.error("JSON 직렬화 오류 발생: {}", e.getMessage(), e);
        }

        emitters.removeAll(deadEmitters);
        log.info("연결 정리 후 클라이언트 수: {}", emitters.size());
    }

    // 모든 사용자에게 알림을 DB에 저장
    private void saveNotificationsForAllUsers(NoticeDTO noticeDTO) {
        log.info("모든 사용자에게 알림 저장 시작 - 공지사항 제목: {}, 공지사항 ID: {}", noticeDTO.getTitle(), noticeDTO.getNoticeId());
        List<Integer> userIds = notificationMapper.getAllUserIds();
        log.info("조회된 사용자 ID 목록: {}", userIds);
        for (Integer userId : userIds) {
            NotificationDTO notification = new NotificationDTO();
            notification.setUserId(userId);
            notification.setType(NotificationType.NOTICE.getCode());
            notification.setMessage("새로운 공지사항이 있습니다.");
            notification.setRelatedId(noticeDTO.getNoticeId());
            log.info("사용자 ID: {}에 대한 알림 저장 시작, 메시지: {}", userId, notification.getMessage());
            notificationMapper.insertNotification(notification);
            log.info("사용자 ID: {}에 대한 알림 저장 완료", userId);
        }
    }

    // 사용자 ID로 알림 목록을 조회
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
}