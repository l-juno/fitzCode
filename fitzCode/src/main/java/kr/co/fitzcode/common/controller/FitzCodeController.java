package kr.co.fitzcode.common.controller;

import kr.co.fitzcode.admin.service.SearchLogService;
import kr.co.fitzcode.common.dto.CustomOAuth2User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FitzCodeController {

    private final SearchLogService searchLogService;

    @GetMapping("/")
    public String mainPage() {
        return "fitzCode";
    }

    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam("keyword") String keyword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User)
                ? ((CustomOAuth2User) authentication.getPrincipal()).getUserId()
                : 0;

        searchLogService.saveSearchLog(userId, keyword);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "검색이 성공적으로 처리되었습니다.");
        response.put("keyword", keyword);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/result")
    public String searchResult(@RequestParam("keyword") String keyword) {
        return "searchResult";
    }

    @PostMapping("/admin/extend-session")
    @ResponseBody
    public ResponseEntity<String> extendSession(HttpSession session) {
        try {
            session.setMaxInactiveInterval(1800); // 30분 (1800초)
            return ResponseEntity.ok("Session extended successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to extend session");
        }
    }

    @GetMapping("/admin/session-info")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSessionInfo(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        long creationTime = session.getCreationTime(); // 세션 생성 시간 (밀리초)
        int maxInactiveInterval = session.getMaxInactiveInterval(); // 최대 비활성 간격 (초)
        int lastAccessedTime = (int) (session.getLastAccessedTime() / 1000); // 마지막 접근 시간 (초 단위)
        int currentTime = (int) (System.currentTimeMillis() / 1000); // 현재 시간 (초 단위)
        int timeLeft = maxInactiveInterval - (currentTime - lastAccessedTime); // 남은 시간
        response.put("creationTime", creationTime);
        response.put("timeLeft", Math.max(timeLeft, 0)); // 음수 방지
        return ResponseEntity.ok(response);
    }
}