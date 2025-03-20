package kr.co.fitzcode.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = authentication.getName(); // 로그인한 사용자 이메일
        UserDTO user = userService.findByEmail(email);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("dto", user); // 세션에 dto 저장
            System.out.println("Login successful, session set: " + user.getUserName());
        }
        response.sendRedirect("/");
    }
}