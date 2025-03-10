package kr.co.fitzcode.common.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VisitorDTO {
    private Long id;                  // 방문 로그 ID
    private Long userId;              // 로그인한 사용자 ID
    private String visitorId;         // 비회원 UUID
    private LocalDateTime visitTime;  // 방문 시간
    private String pageUrl;           // 방문한 페이지 URL
    private String referrerUrl;       // 유입 경로
    private String userAgent;         // 브라우저/OS 정보
    private Integer deviceType;       // 디바이스 타입 (1: MOBILE, 2: DESKTOP)
    private String ipAddress;         // IP 주소
    private LocalDate visitDate;      // 날짜별 집계용
    private Integer count;            // 방문자 수
}