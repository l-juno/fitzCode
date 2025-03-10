package kr.co.fitzcode.common.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123";
        String encodedPassword = "$2a$10$4OL75O2RKvpOn9eADWUcOu/fmGdtqAX37w4Q1mslbplemW7k/4zxi";
        System.out.println(encoder.matches(rawPassword, encodedPassword));
    }
}
