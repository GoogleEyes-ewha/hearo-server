package com.gdsc.hearo.domain.item.service;

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
}
