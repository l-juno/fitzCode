package kr.co.fitzcode.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    private String bankName;       // 은행명
    private String accountNumber;  // 계좌번호
    private String accountHolder;  // 계좌주
}