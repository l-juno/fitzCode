package kr.co.fitzcode.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDTO {
    private int userId;
    private String userName;
    private String email;
    private LocalDateTime createdAt;
}