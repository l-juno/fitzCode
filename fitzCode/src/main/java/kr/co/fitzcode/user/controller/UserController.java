package kr.co.fitzcode.user.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.fitzcode.common.dto.EmailMessageDTO;
import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.user.service.EmailService;
import kr.co.fitzcode.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String loginPage(Model model) {
        if (model.containsAttribute("error")) {
            model.addAttribute("ErrorMessage", "입력한 이메일 또는 비밀번호가 잘못되었습니다.");
        }
        return "user/login";
    }

    @PostMapping("/login")
    public String loginOK(HttpSession session,
                          @RequestParam("email") String email,
                          @RequestParam("password") String password,
                          Model model) {

        UserDTO dto = userService.findByEmail(email);
        log.info("userDTO: {}", dto);

        if (dto == null || !passwordEncoder.matches(password, dto.getPassword())) {
            model.addAttribute("ErrorMessage", "입력한 이메일은 가입 내역이 존재하지 않거나 비밀번호가 틀립니다.");
            return "user/login";
        }

        session.setAttribute("dto", dto);
        System.out.println("이메일 >>>>>" + dto.getEmail());
        System.out.println("비밀번호 >>>>>" + dto.getPassword());
        System.out.println("로그인 성공");
        System.out.println("이름 >>>>>" + dto.getUserName());

        return "redirect:/";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/";
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
            return "user/joinForm";
        }
        boolean nicknameCheck = userService.nicknameDuplicate(dto.getNickname());
        if (nicknameCheck) {
            model.addAttribute("nicknameCheckError", "이미 사용 중인 닉네임 입니다.");
            return "user/joinForm";
        }
        boolean phoneNumberCheck = userService.phoneNumberDuplicate(dto.getPhoneNumber());
        if (phoneNumberCheck) {
            model.addAttribute("phoneNumberCheckError", "이미 사용 중인 전화번호 입니다.");
            return "user/joinForm";
        }

        EmailMessageDTO emailMessage = EmailMessageDTO.builder()
                .to(dto.getEmail())
                .subject("이메일 인증을 위한 인증 코드 발송") // 메일 제목 부분
                .build();

        String code = emailService.sendEmail(emailMessage, "user/joinEmailView");

        session.setAttribute("userDTO", dto);

        session.setAttribute("authCode", code);
        model.addAttribute("code", code);

        return "user/joinEmail";
    }

    @PostMapping("/joinSuccess")
    public String joinEmailCode(@RequestParam("authCode") String authCode, HttpSession session, Model model) {

        String sessionCode = (String) session.getAttribute("authCode");
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        if (sessionCode == null || userDTO == null || !Objects.equals(sessionCode, authCode)) {
            model.addAttribute("authCodeError", "인증 코드가 일치하지 않거나 세션이 만료되었습니다.");
            return "user/joinEmail";
        }

        userService.registerUser(userDTO);

        session.removeAttribute("authCode");
        session.removeAttribute("userDTO");

        model.addAttribute("userName", userDTO.getUserName());

        return "user/joinSuccess";
    }


    // 비밀번호 찾기 페이지 이동
    @GetMapping("/pwEmail")
    public String pwFind() {
        return "user/findpwEmail";
    }

    // 비밀번호 재설정 이메일 발송
    @PostMapping("pwEmail")
    public String pwEmail(@RequestParam("email") String email, UserDTO dto, Model model, HttpSession session) {

        boolean checkEmail = userService.emailDuplicate(email);

        if (!checkEmail) {
            model.addAttribute("errorMessage", "입력한 이메일은 가입 내역이 존재하지 않습니다.");
            return "user/findpwEmail";
        }

        EmailMessageDTO emailMessage = EmailMessageDTO.builder()
                .to(email)
                .subject("비밀번호 재설정 메일 발송") // 메일 제목 부분
                .build();

        String code = emailService.sendEmail(emailMessage, "user/findpwEmailView");
        model.addAttribute("email", email);
        model.addAttribute("dto", dto);

        session.setAttribute("email", email);

        return "user/findpwEmailSuccess";
    }

    // 비밀번호 재설정 페이지 이동
    @GetMapping("/resetPw")
    public String resetPw(HttpSession session, UserDTO dto, Model model) {
        String email = (String) session.getAttribute("email");

        if (email == null) {
            model.addAttribute("errorMessage", "세션이 만료되었습니다. 이메일을 다시 입력해주세요.");
            return "redirect:/user/findpwEmail";
        }

        model.addAttribute("email", email);
        model.addAttribute("dto", dto);
        System.out.println("email>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + email);
        return "user/resetPw";
    }

    // 비밀번호 재설정
    @PostMapping("/resetPwSuccess")
    public String resetPw(@ModelAttribute("dto") UserDTO dto, BindingResult bindingResult,
                          @RequestParam("email") String email,
                          RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/resetPw";
        }

        if (email == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "세션이 만료되었습니다. 이메일을 다시 입력해주세요.");
            return "redirect:/user/findpwEmail";
        }

        dto.setPassword(dto.getPassword());
        userService.updatePw(dto);
        model.addAttribute("userName", dto.getUserName());


        return "user/resetPwSuccess";
    }

}