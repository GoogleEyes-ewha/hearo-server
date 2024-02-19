package com.gdsc.hearo.domain.item.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Long itemId; // 기본키

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "item_info", nullable = false)
    private String itemInfo;

    @Column(name = "item_url", nullable = false)
    private String itemUrl;

    @Column(name = "item_img", nullable = false)
    private String item_img;

    @Column(name = "price")
    private Integer price;

    @Column(name = "allergy")
    private String allergy;

    @Column(name = "detail_img")
    private String detailImg;

    @Column(name = "nutrition_img")
    private String nutritionImg;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
