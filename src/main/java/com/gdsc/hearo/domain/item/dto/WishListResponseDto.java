package com.gdsc.hearo.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishListResponseDto {

    private Integer itemCount;
    private List<ItemDto> wishList;
}
