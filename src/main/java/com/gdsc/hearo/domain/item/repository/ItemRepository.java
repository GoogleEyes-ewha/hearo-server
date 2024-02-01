package com.gdsc.hearo.domain.item.repository;

import com.gdsc.hearo.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByCategoryCategoryId(Long categoryId);
}
