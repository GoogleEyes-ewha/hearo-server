package com.gdsc.hearo.domain.review.repository;

import com.gdsc.hearo.domain.review.entity.ReviewTts;
import com.gdsc.hearo.global.common.VoiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewTtsRepository extends JpaRepository<ReviewTts, Long> {
    ReviewTts findTopByItemItemIdAndReviewTypeAndVoiceTypeOrderByCreatedAtDesc(Long itemId, ReviewTts.ReviewType reviewType, VoiceType voiceType);
}
