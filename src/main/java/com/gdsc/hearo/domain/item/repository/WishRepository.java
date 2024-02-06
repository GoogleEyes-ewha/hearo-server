package com.gdsc.hearo.domain.item.repository;

import com.gdsc.hearo.domain.item.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish,Long> {

    List<Wish> findByMember_MemberId(Long memberId);
}
