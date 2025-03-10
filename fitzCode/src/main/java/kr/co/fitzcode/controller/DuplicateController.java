package kr.co.fitzcode.controller;

import kr.co.fitzcode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DuplicateController {

    private final UserService userService;

    @GetMapping("/checkEmail")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        boolean isDuplicate = userService.emailDuplicate(email);
        if (isDuplicate) {
            return ResponseEntity.ok().body("사용 중인 이메일입니다.");
        } else {
            return ResponseEntity.ok().body("사용 가능한 이메일입니다!");
        }
    }

    @GetMapping("/checkNickname")
    public ResponseEntity<String> checkNickname(@RequestParam String nickname) {
        boolean isDuplicate = userService.nicknameDuplicate(nickname);
        if (isDuplicate) {
            return ResponseEntity.ok().body("사용 중인 닉네임입니다.");
        } else {
            return ResponseEntity.ok().body("사용 가능한 닉네임입니다!");
        }
    }


}
