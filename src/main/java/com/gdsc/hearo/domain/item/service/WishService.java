package com.gdsc.hearo.domain.item.service;

import com.gdsc.hearo.domain.item.dto.ItemDto;
import com.gdsc.hearo.domain.item.dto.WishListResponseDto;
import com.gdsc.hearo.domain.item.dto.WishRequestDto;
import com.gdsc.hearo.domain.item.dto.WishResponseDto;
import com.gdsc.hearo.domain.item.entity.Item;
import com.gdsc.hearo.domain.item.entity.Wish;
import com.gdsc.hearo.domain.item.repository.ItemRepository;
import com.gdsc.hearo.domain.item.repository.WishRepository;
import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.member.repository.MemberRepository;
import com.gdsc.hearo.domain.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ItemRepository itemRepository;
    private  final MemberRepository memberRepository;

    @Autowired
    public WishService(WishRepository wishRepository,ItemRepository itemRepository, MemberRepository memberRepository){
        this.wishRepository = wishRepository;
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
    }

    public WishResponseDto addToWishList(Long userId, Long itemId){

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        Wish wish = Wish.builder()
                .item(item)
                .member(member)
                .build();
        wishRepository.save(wish);

        return WishResponseDto.builder()
                .result("상품이 위시리스트에 담겼습니다.")
                .build();
    }

    public WishListResponseDto getWishList(Long userId){
        Optional<Member> member = memberRepository.findById(userId);
        if(member.isPresent()){
            List<Wish> wishList = wishRepository.findByMember_MemberId(userId);

            List<ItemDto> itemDtoList = wishList.stream()
                    .map(wish -> ItemDto.builder()
                            .id(wish.getItem().getItemId())
                            .name(wish.getItem().getName())
                            .img(wish.getItem().getItem_img())
                            .info(wish.getItem().getItemInfo())
                            .price(wish.getItem().getPrice())
                            .build())
                    .collect(Collectors.toList());

            return WishListResponseDto.builder()
                    .itemCount(itemDtoList.size())
                    .wishList(itemDtoList)
                    .build();

        }else {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }


    }
}
