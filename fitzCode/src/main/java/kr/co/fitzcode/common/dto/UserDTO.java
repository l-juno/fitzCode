package kr.co.fitzcode.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private int userId;

    @NotNull(message = "사용자 이름은 필수입니다.")
    private String userName;

    private String nickname;

    @NotNull(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotNull(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 최소 8자 이상 최대 16자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "비밀번호는 영문자와 숫자로만 구성되어야 합니다.")
    private String password;

    private String phoneNumber;
    @Size(min = 8, max = 8, message = "생년월일은 8자로 입력해주세요.")
    private String birthDate;
    private String kakaoId;
    private String naverId;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 추가
    private int roleId;
}