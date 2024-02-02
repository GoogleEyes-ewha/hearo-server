package com.gdsc.hearo.domain.item.service;

import com.gdsc.hearo.domain.item.dto.CategoryResponseDto;

import java.util.List;

public interface ItemService {
    List<CategoryResponseDto> getItemByCategory(Long categoryId);
}
