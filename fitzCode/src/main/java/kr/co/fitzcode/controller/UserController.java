package kr.co.fitzcode.controller;

import jakarta.validation.Valid;
import kr.co.fitzcode.dto.UserDto;
import kr.co.fitzcode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService useService;

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    // 회원가입 페이지 이동
    @GetMapping("/joinForm")
    public String joinForm(Model model, UserDto dto) {
        model.addAttribute("dto", dto);
        return "user/joinForm";
    }

    // 회원가입 이메일 임시 코드 발급
    @PostMapping("/joinForm")
    public String joinNum(@Valid @ModelAttribute("dto") UserDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/joinForm";
        }

        // 중복 체크 (이메일, 전화번호, 닉네임)
        boolean emailCheck = useService.emailDuplicate(dto.getEmail());
        if (emailCheck) {
            model.addAttribute("emailCheckError", "이미 사용 중인 이메일 입니다.");
        }
        boolean nickNameCheck = useService.nickNameDuplicate(dto.getNickname());
        if (nickNameCheck) {
            model.addAttribute("nickNameCheckError", "이미 사용 중인 닉네임 입니다.");
        }
        boolean phoneNumberCheck = useService.phoneNumberDuplicate(dto.getPhoneNumber());
        if (phoneNumberCheck) {
            model.addAttribute("phoneNumberCheckError", "이미 사용 중인 전화번호 입니다.");
        }

        //useService.insertUser(dto);
        return "redirect:/user/joinNum";
    }

    // 비밀번호 찾기 페이지 이동
    @GetMapping("/pwEmail")
    public String pwFind() {
        return "user/pwEmail";
    }

}