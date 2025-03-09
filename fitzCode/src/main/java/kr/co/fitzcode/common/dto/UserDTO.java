package kr.co.fitzcode.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int userId;
    private String userName;
    private String nickname;
    private String email;
    private String password;
    private String phoneNumber;
    private String birthDate;
    private String kakaoId;
    private String naverId;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int roleId;

}
