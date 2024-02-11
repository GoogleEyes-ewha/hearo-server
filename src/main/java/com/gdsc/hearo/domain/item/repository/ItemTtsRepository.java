package com.gdsc.hearo.domain.item.repository;

import com.gdsc.hearo.domain.item.entity.ItemTts;
import com.gdsc.hearo.global.common.VoiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTtsRepository extends JpaRepository<ItemTts, Long> {

    ItemTts findTopByItemItemIdAndVoiceTypeOrderByCreatedAtDesc(
            Long itemId, VoiceType voiceType);
}
