package com.gdsc.hearo.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewTTSRequestDto {
    private String positiveReviewUrl;
    private String negativeReviewUrl;
}
