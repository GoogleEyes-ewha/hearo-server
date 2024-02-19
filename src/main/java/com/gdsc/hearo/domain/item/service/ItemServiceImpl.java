package com.gdsc.hearo.domain.item.service;

import com.gdsc.hearo.domain.item.dto.ItemDetailResponseDto;
import com.gdsc.hearo.domain.item.dto.ItemDto;
import com.gdsc.hearo.domain.item.entity.Item;
import com.gdsc.hearo.domain.item.entity.Wish;
import com.gdsc.hearo.domain.item.repository.ItemRepository;
import com.gdsc.hearo.domain.item.repository.WishRepository;
import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService  {

    private final ItemRepository itemRepository;
    private final DocumentAiService documentAiService;

    private final MemberRepository memberRepository;

    private final WishRepository wishRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, DocumentAiService documentAiService,
                           MemberRepository memberRepository,WishRepository wishRepository) {
        this.itemRepository = itemRepository;
        this.documentAiService = documentAiService;
        this.memberRepository = memberRepository;
        this.wishRepository = wishRepository;

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


    public ItemDetailResponseDto getItemDetailById(Long itemId,Long userId){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));

        // 여기에 Document AI API를 이용하여 nutritionImg의 텍스트를 추출하는 로직 추가
        String nutritionText = null;
        try{
            nutritionText = documentAiService.execute(item.getNutritionImg());
        }catch (Exception e) {
            e.printStackTrace();
        }

        boolean isWish = isItemInWishlist(itemId,userId);

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
                .isWish(isWish)  // 필요에 따라 설정
                .build();

        return itemDetailResponse;
    }

    private boolean isItemInWishlist(Long itemId, Long userId) {
        // 사용자 정보를 조회
        Optional<Member> member = memberRepository.findById(userId);
        if (member.isPresent()) {
            // 사용자의 위시리스트를 조회
            List<Wish> wishList = wishRepository.findByMember_MemberId(userId);
            // 해당 상품이 위시리스트에 있는지 여부를 반환
            return wishList.stream().anyMatch(wish -> wish.getItem().getItemId().equals(itemId));
        } else {
            // 사용자가 없는 경우에는 상품이 위시리스트에 있을 수 없음
            return false;
        }
    }
}
