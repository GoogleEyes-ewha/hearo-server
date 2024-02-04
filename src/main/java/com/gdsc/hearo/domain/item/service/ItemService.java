package com.gdsc.hearo.domain.item.service;

import com.gdsc.hearo.domain.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItemByCategory(Long categoryId);//카테고리 검색
    List<ItemDto> getItemByKeyword(String keyword);//키워드 검색
}
