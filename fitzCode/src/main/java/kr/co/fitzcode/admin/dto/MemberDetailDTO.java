package kr.co.fitzcode.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDetailDTO {
    private int userId;                  // 사용자 ID
    private String userName;             // 사용자 이름
    private String nickname;             // 닉네임
    private String email;                // 이메일 주소
    private String phoneNumber;          // 전화번호
    private String birthDate;            // 생년월일 (YYYY-MM-DD 형식)

    private String kakaoId;              // 카카오 로그인 ID (소셜 로그인)
    private String naverId;              // 네이버 로그인 ID (소셜 로그인)
    private String profileImage;         // 프로필 이미지 URL

    private LocalDateTime createdAt;     // 회원 가입 날짜
    private LocalDateTime updatedAt;     // 회원 정보 수정 날짜

    private int tierLevel;               // 회원 등급 (1-브론즈, 2-실버, 3-골드, 4-플레티넘)
    private int totalSpent;              // 총 구매 금액
    private LocalDateTime tierUpdatedAt; // 등급 마지막 업데이트 날짜
}