package com.gdsc.hearo.domain.item.entity;

import com.gdsc.hearo.domain.review.entity.ReviewTts;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Entity
@Table(name = "item_tts")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
public class ItemTts {

    public enum VoiceType {
        MALE_VOICE, FEMALE_VOICE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_tts_id", nullable = false)
    private Long itemTtsId; // 기본키

    @Column(name = "tts_file", nullable = false)
    private String ttsFile;

    @Enumerated(EnumType.STRING)
    @Column(name = "voice_type", nullable = false)
    private ReviewTts.VoiceType voiceType;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
}
