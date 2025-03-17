package kr.co.fitzcode.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesRankingDTO {
    private String productName;
    private int totalSales;
    private int quantitySold;
    private String imageUrl;
}