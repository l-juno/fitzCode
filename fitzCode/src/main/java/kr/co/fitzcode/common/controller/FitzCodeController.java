package kr.co.fitzcode.common.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.fitzcode.admin.service.ProductService;
import kr.co.fitzcode.admin.service.SearchLogService;
import kr.co.fitzcode.common.dto.CustomOAuth2User;
import kr.co.fitzcode.common.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FitzCodeController {

    private final SearchLogService searchLogService;
    private final ProductService productService;

    @GetMapping("/")
    public String mainPage() {
        return "fitzCode";
    }

    @PostMapping("/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> search(@RequestParam("keyword") String keyword) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Integer userId = null;
            if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User) {
                userId = ((CustomOAuth2User) authentication.getPrincipal()).getUserId();
            }

            searchLogService.saveSearchLog(userId, keyword);

            response.put("success", true);
            response.put("message", "검색이 성공적으로 처리되었습니다.");
            response.put("keyword", keyword);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("검색 처리 중 오류 발생: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "검색 처리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/search/result")
    public String searchResult(@RequestParam("keyword") String keyword) {
        return "searchResult";
    }

    @GetMapping("/api/product/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchProducts(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 상품 검색 로직
            List<ProductDTO> products = productService.searchProducts(keyword, page, 20);
            int totalLength = productService.countAllProducts(keyword);

            response.put("list", products);
            response.put("totalLength", totalLength);
            response.put("currentPage", page);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("상품 검색 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<>());
        }
    }

    @PostMapping("/admin/extend-session")
    @ResponseBody
    public ResponseEntity<String> extendSession(HttpSession session) {
        try {
            session.setMaxInactiveInterval(1800);
            return ResponseEntity.ok("Session extended successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to extend session");
        }
    }

    @GetMapping("/admin/session-info")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSessionInfo(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        long creationTime = session.getCreationTime();
        int maxInactiveInterval = session.getMaxInactiveInterval();
        long lastAccessedTime = session.getLastAccessedTime();
        long currentTime = System.currentTimeMillis();
        int timeLeft = (int) ((maxInactiveInterval * 1000 - (currentTime - lastAccessedTime)) / 1000);

        response.put("creationTime", creationTime);
        response.put("timeLeft", Math.max(timeLeft, 0));
        return ResponseEntity.ok(response);
    }
}