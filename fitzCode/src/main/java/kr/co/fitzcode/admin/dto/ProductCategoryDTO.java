package kr.co.fitzcode.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCategoryDTO {
    private Long categoryId;
    private Long parentId;
    private String code;
    private String name;
    private Integer depth;
}