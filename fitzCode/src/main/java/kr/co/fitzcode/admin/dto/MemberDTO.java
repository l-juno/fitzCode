package kr.co.fitzcode.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDTO {
    private int userId;                 // 사용자 ID
    private String userName;            // 사용자 이름
    private String email;               // 이메일 주소
    private LocalDateTime createdAt;    // 회원 가입 날짜
}