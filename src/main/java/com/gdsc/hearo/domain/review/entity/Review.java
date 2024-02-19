package com.gdsc.hearo.domain.review.entity;

import com.gdsc.hearo.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId; // 기본키

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
}
