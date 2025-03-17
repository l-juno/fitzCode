package kr.co.fitzcode.common.controller;

import kr.co.fitzcode.admin.service.SearchLogService;
import kr.co.fitzcode.common.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

//        System.out.println("Search response: " + response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/result")
    public String searchResult(@RequestParam("keyword") String keyword) {
        return "searchResult";
    }
}