package kr.co.fitzcode.admin.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info(">>> CustomLoginSuccessHandler 호출");
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        // 접근 권한이 없는 경로로 직접 접근한 경우 (savedRequest != null)
        // 로그인 폼으로 간 후 로그인 폼으로 가기 전 페이지로 다시 리다이렉트
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            log.info(">>> redirectUrl:{}", targetUrl);
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }

        // 로그인 페이지에서 접근한 경우
        else {
            HttpSession session = request.getSession();
            String prevPage = (String) session.getAttribute("prevPage");
            redirectStrategy.sendRedirect(request, response, prevPage);

        }


    }
}
