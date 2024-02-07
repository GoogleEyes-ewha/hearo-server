package com.gdsc.hearo.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishRequestDto {

    private Long itemId;
}
//상품 위시리스트에 추가할때도 삭제할 때도 쓰임