package com.gdsc.hearo.domain.item.dto;

import com.gdsc.hearo.domain.item.entity.Item;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String img;
    private String info;
    private String price;

}
