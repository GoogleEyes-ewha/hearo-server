package com.gdsc.hearo.domain.item.service;

import com.gdsc.hearo.domain.item.dto.ItemDetailResponseDto;
import com.gdsc.hearo.domain.item.dto.ItemDto;
import com.gdsc.hearo.domain.item.entity.Item;
import com.gdsc.hearo.domain.item.repository.ItemRepository;
import com.google.api.gax.rpc.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService  {

    private final ItemRepository itemRepository;
    private final DocumentAiService documentAiService;

    public ItemServiceImpl(ItemRepository itemRepository, DocumentAiService documentAiService) {
        this.itemRepository = itemRepository;
        this.documentAiService = documentAiService;
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

    public class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }
    public ItemDetailResponseDto getItemDetailById(Long itemId){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));

        // 여기에 Document AI API를 이용하여 nutritionImg의 텍스트를 추출하는 로직 추가
        String nutritionText = null;
        try{
            nutritionText = documentAiService.execute(item.getNutritionImg());
        }catch (Exception e) {
            e.printStackTrace();
        }

        ItemDetailResponseDto itemDetailResponse = ItemDetailResponseDto.builder()
                .name(item.getName())
                .itemInfo(item.getItemInfo())
                .price(item.getPrice())
                .itemImg(item.getItem_img())
                .detailImg(item.getDetailImg())
                .allergy(item.getAllergy())
                .nutritionImg(item.getNutritionImg())
                .nutritionText(nutritionText)  // Document AI API 호출 결과로 얻은 텍스트를 여기에 설정
                .kurlyUrl(item.getItemUrl())
                .isWish(false)  // 필요에 따라 설정
                .build();

        return itemDetailResponse;
    }
}
