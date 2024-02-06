package com.gdsc.hearo.domain.item.service;

import com.gdsc.hearo.domain.item.dto.ItemDto;
import com.gdsc.hearo.domain.item.entity.Item;
import com.gdsc.hearo.domain.item.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService  {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemDto> getItemByCategory(Long categoryId){
        List<Item> items = itemRepository.findByCategoryCategoryId(categoryId);
        return items.stream().map(this::convertToCategoryDto).collect(Collectors.toList());
    }

    public List<ItemDto> getItemByKeyword (String keyword){
       List<Item> items = itemRepository.findByNameContainingIgnoreCase(keyword);
       return items.stream().map(item -> convertToSearchDto(item,keyword)).collect(Collectors.toList());
    }

    private ItemDto convertToCategoryDto(Item item){
        return new ItemDto(
                item.getItemId(),
                item.getName(),
                item.getItem_img(),
                item.getItemInfo(),
                item.getPrice()
        );
    }

    private ItemDto convertToSearchDto(Item item, String keyword) {
        return new ItemDto(
                item.getItemId(),
                item.getName(),
                item.getItem_img(),
                item.getItemInfo(),
                item.getPrice()
        );
    }

    /*@Override
    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 없습니다."));
    }*/
}
