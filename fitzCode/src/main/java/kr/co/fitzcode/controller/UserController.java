package kr.co.fitzcode.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.fitzcode.dto.EmailMessageDTO;
import kr.co.fitzcode.dto.EmailPostDTO;
import kr.co.fitzcode.dto.UserDTO;
import kr.co.fitzcode.service.EmailService;
import kr.co.fitzcode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    // 회원가입 페이지 이동
    @GetMapping("/joinForm")
    public String joinForm(Model model, UserDTO dto) {
        model.addAttribute("dto", dto);
        return "user/joinForm";
    }

    // 회원가입 이메일 임시 코드 발급
    @PostMapping("/joinForm")
    public String joinNum(@ModelAttribute("dto") UserDTO dto,
                          BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "user/joinForm";
        }

        // 중복 체크 (이메일, 닉네임, 전화번호)
        boolean emailCheck = userService.emailDuplicate(dto.getEmail());
        if (emailCheck) {
            model.addAttribute("emailCheckError", "이미 사용 중인 이메일 입니다.");
            System.out.println("이미 사용 중인 이메일");
            return "user/joinForm";
        }
        boolean nickNameCheck = userService.nickNameDuplicate(dto.getNickName());
        if (nickNameCheck) {
            model.addAttribute("nickNameCheckError", "이미 사용 중인 닉네임 입니다.");
            System.out.println("이미 사용 중인 닉네임");
            return "user/joinForm";
        }
        boolean phoneNumberCheck = userService.phoneNumberDuplicate(dto.getPhoneNumber());
        if (phoneNumberCheck) {
            model.addAttribute("phoneNumberCheckError", "이미 사용 중인 전화번호 입니다.");
            System.out.println("이미 사용 중인 전화번호");
            return "user/joinForm";
        }

        EmailMessageDTO emailMessage = EmailMessageDTO.builder()
                .to(dto.getEmail())
                .subject("이메일 인증을 위한 인증 코드 발송") // 메일 제목 부분
                .build();
        String code = emailService.sendEmail(emailMessage, "user/joinEmailView");

        session.setAttribute("userDTO", dto);

        System.out.println("전화번호 >>>>>>>>>>>>>" + dto.getPhoneNumber());
        System.out.println("생년월일 >>>>>>>>>>>>>" + dto.getBirthDate());

        session.setAttribute("authCode", code);
        model.addAttribute("code", code);
        return "user/joinEmail";
    }

    // 이메일 인증 코드 확인 후 회원가입 완료
    @PostMapping("/joinOk")
    public String verifyEmailCode(@RequestParam("authCode") String authCode, HttpSession session, Model model) {

        String sessionCode = (String) session.getAttribute("authCode");
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        if (sessionCode == null || !sessionCode.equals(authCode)) {
            model.addAttribute("authCodeError", "인증 코드가 일치하지 않습니다.");
            return "user/joinEmail";
        }

        // 인증 코드가 일치하면 사용자 정보를 DB에 저장
        userService.insertUser(userDTO);

        session.removeAttribute("authCode");
//        session.removeAttribute("userDTO");

        // 회원가입 완료 페이지로 이동
        return "user/joinSuccess";
    }


    // 비밀번호 찾기 페이지 이동
    @GetMapping("/pwEmail")
    public String pwFind() {
        return "pwEmailView";
    }


}