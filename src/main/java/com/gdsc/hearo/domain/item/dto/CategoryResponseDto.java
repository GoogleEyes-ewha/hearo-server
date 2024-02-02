package com.gdsc.hearo.domain.item.dto;
import lombok.Data;

@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String img;
    private String info;
    private String price;

}
