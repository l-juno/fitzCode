package kr.co.fitzcode.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ChartDataDTO {
    private List<Integer> revenueData; // 30일간 총 매출
    private List<Integer> volumeData;  // 30일간 총 판매 건수

    public ChartDataDTO() {
        this.revenueData = new ArrayList<>(Collections.nCopies(30, 0));
        this.volumeData = new ArrayList<>(Collections.nCopies(30, 0));
    }
}