package kr.co.fitzcode.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MemberDetailDTO {
    private int userId;                  // 사용자 ID
    private String userName;             // 이름
    private String nickname;             // 닉네임
    private String email;                // 이메일
    private String phoneNumber;          // 전화번호
    private String birthDate;            // 생년월일
    private String kakaoId;              // 카카오 ID
    private String naverId;              // 네이버 ID
    private String profileImage;         // 프로필 이미지
    private LocalDateTime createdAt;     // 가입 날짜
    private LocalDateTime updatedAt;     // 수정 날짜
    private int tierLevel;               // 등급
    private int totalSpent;              // 총 구매 금액
    private LocalDateTime tierUpdatedAt; // 등급 업데이트 날짜

    private AddressDTO defaultAddress;   // 기본 주소
    private AccountDTO defaultAccount;   // 기본 계좌
    private List<OrderDTO> orders;       // 주문 내역
}