package com.gdsc.hearo.domain.item.service;

import com.gdsc.hearo.domain.item.dto.CategoryResponseDto;
import com.gdsc.hearo.domain.item.entity.Item;
import com.gdsc.hearo.domain.item.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService  {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<CategoryResponseDto> getItemByCategory(Long categoryId){
        List<Item> items = itemRepository.findByCategoryCategoryId(categoryId);
        return items.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private CategoryResponseDto convertToDto(Item item){
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(item.getItemId());
        categoryResponseDto.setName(item.getName());
        categoryResponseDto.setImg(item.getItem_img());
        categoryResponseDto.setInfo(item.getItemInfo());
        categoryResponseDto.setPrice(item.getPrice().toString());
        return categoryResponseDto;
    }
}
