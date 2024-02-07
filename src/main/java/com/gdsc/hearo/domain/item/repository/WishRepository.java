package com.gdsc.hearo.domain.item.repository;

import com.gdsc.hearo.domain.item.entity.Item;
import com.gdsc.hearo.domain.item.entity.Wish;
import com.gdsc.hearo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish,Long> {

    List<Wish> findByMember_MemberId(Long memberId);

    //void deleteByMember_MemberIdAndItem_ItemId(Long memberId, Long itemId);
    void deleteByMemberAndItem(Member member,Item item);
}
