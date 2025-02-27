package kr.co.fitzcode.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDetailDTO {
    private int userId;
    private String userName;
    private String nickname;
    private String email;
    private String phoneNumber;
    private String birthDate;
    private String kakaoId;
    private String naverId;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int tierLevel;
    private int totalSpent;
    private LocalDateTime tierUpdatedAt;
}