package kr.co.fitzcode.common.controller;

import kr.co.fitzcode.admin.service.SearchLogService;
import kr.co.fitzcode.common.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class FitzCodeController {

    private final SearchLogService searchLogService;

    @GetMapping("/")
    public String mainPage() {
        return "fitzCode";
    }

    @PostMapping("/search")
    @ResponseBody
    public String search(@RequestParam("keyword") String keyword,
                         @AuthenticationPrincipal CustomUserDetails user) {
        int userId = (user != null) ? user.getUserId() : 0;
        searchLogService.saveSearchLog(userId, keyword);
        return "success";
    }

    @GetMapping("/search/result")
    public String searchResult(@RequestParam("keyword") String keyword) {
        return "searchResult";
    }
}