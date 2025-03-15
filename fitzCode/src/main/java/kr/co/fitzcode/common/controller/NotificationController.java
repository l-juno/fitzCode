package kr.co.fitzcode.common.controller;

import kr.co.fitzcode.common.dto.CustomOAuth2User;
import kr.co.fitzcode.common.dto.NotificationDTO;
import kr.co.fitzcode.common.service.CustomUserDetails;
import kr.co.fitzcode.common.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<String> createNotification(@RequestBody NotificationDTO notificationDTO, Principal principal) {
        log.info("POST /api/notifications 요청 처리");
        if (principal == null) {
            log.warn("Principal이 null입니다 - 인증되지 않은 접근");
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\", \"message\": \"Authentication required\"}");
        }

        int userId = extractUserId(principal);
        notificationDTO.setUserId(userId);
        return ResponseEntity.ok("알림 생성 완료");
    }

    @GetMapping
    public ResponseEntity<?> getNotifications(Principal principal) {
        log.info("GET /api/notifications 요청 처리");
        if (principal == null) {
            log.warn("Principal이 null입니다 - 인증되지 않은 접근");
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\", \"message\": \"Authentication required\"}");
        }

        int userId = extractUserId(principal);
        log.info("userId {}에 대한 알림 조회", userId);
        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        log.info("userId {}에 대한 알림 반환: {}", userId, notifications);
        return ResponseEntity.ok(notifications);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable int id, Principal principal) {
        log.info("DELETE /api/notifications/{} 요청 처리", id);
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("알림 삭제 완료");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllNotifications(Principal principal) {
        log.info("DELETE /api/notifications 요청 처리");
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        int userId = extractUserId(principal);
        notificationService.deleteAllNotifications(userId);
        return ResponseEntity.ok("모든 알림 삭제 완료");
    }

    @PostMapping("/read")
    public ResponseEntity<String> markAllAsRead(Principal principal) {
        log.info("POST /api/notifications/read 요청 처리");
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        int userId = extractUserId(principal);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok("모든 알림 읽음 처리 완료");
    }

    // Principal에서 userId를 추출하는 헬퍼 메서드
    private int extractUserId(Principal principal) {
        Object principalObj = principal;

        // UsernamePasswordAuthenticationToken 또는 OAuth2AuthenticationToken 처리
        if (principalObj instanceof UsernamePasswordAuthenticationToken) {
            principalObj = ((UsernamePasswordAuthenticationToken) principalObj).getPrincipal();
        } else if (principalObj instanceof OAuth2AuthenticationToken) {
            principalObj = ((OAuth2AuthenticationToken) principalObj).getPrincipal();
        }

        // 내부 Principal 객체에서 userId 추출
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