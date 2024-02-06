package com.gdsc.hearo.domain.item.repository;

import com.gdsc.hearo.domain.item.entity.Item;
import com.gdsc.hearo.domain.item.entity.Wish;
import com.gdsc.hearo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish,Long> {

    //List<Wish> findByMemberAndItem(Member member, Item item);
}
