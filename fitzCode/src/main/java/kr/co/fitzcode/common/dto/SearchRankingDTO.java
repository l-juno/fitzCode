package kr.co.fitzcode.common.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchRankingDTO {
    private String keyword;        // 검색어
    private long todaySearchCount; // 오늘 검색 횟수
    private int currentRanking;    // 현재 순위
    private int previousRanking;   // 전일 순위
    private LocalDate date;        // Batch에서 사용

    public String getRankingChange() {
        if (previousRanking == 0) return "-";
        int change = previousRanking - currentRanking;
        return change == 0 ? "-" : (change > 0 ? "+" + change : String.valueOf(change));
    }
}