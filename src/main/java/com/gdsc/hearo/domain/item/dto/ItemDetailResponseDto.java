package com.gdsc.hearo.domain.item.dto;

import com.gdsc.hearo.domain.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailResponseDto {


    private String name;
    private String itemInfo;
    private int price;
    private String itemImg;
    private String detailImg;
    private String allergy;
    private String nutritionImg;
    private String nutritionText;
    private String kurlyUrl;
    private boolean isWish;

}
