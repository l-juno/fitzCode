package kr.co.fitzcode.inquiry.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int userId;
    private String username;
    private String nickname;
    private String email;
    private String password;
    private String phonenumber;
    private LocalDateTime birthdate;
    private String kakaoId;
    private String naverId;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}