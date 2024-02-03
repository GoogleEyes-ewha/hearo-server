package com.gdsc.hearo.domain.review.repository;

import com.gdsc.hearo.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByItemItemId(Long itemId);
}
