package kr.co.fitzcode.common.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QnaDTO {
    private Long qnaId;
    private Long productId;
    private Long userId;
    private String userName;
    private String question;
    private String answer;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp answeredAt;
}