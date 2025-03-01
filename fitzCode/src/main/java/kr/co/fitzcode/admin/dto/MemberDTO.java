package kr.co.fitzcode.admin.dto;

import kr.co.fitzcode.common.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MemberDTO {
    private int userId;                 // 사용자 ID
    private String userName;            // 사용자 이름
    private String email;               // 이메일 주소
    private LocalDateTime createdAt;    // 회원 가입 날짜
    private int roleId;                 // 역할 ID (UserRole enum과 매핑)
    private int tierLevel;              // 회원 등급 (1-브론즈, 2-실버, 3-골드, 4-플레티넘)
    private List<Integer> categoryId;   // 카테고리

    // UserRole ->  enum으로 변환
    public UserRole getUserRole() {
        return UserRole.fromCode(this.roleId);
    }
}