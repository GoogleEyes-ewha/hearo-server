package com.gdsc.hearo.domain.review.entity;

import com.gdsc.hearo.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Entity
@Table(name = "review_tts")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
public class ReviewTts {

    public enum ReviewType {
        POSITIVE, NEGATIVE
    }

    public enum VoiceType {
        MALE_VOICE, FEMALE_VOICE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_tts_id", nullable = false)
    private Long itemTtsId; // 기본키

    @Column(name = "tts_file", nullable = false)
    private String ttsFile;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_type", nullable = false)
    private ReviewType reviewType;

    @Enumerated(EnumType.STRING)
    @Column(name = "voice_type", nullable = false)
    private VoiceType voiceType;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
}
