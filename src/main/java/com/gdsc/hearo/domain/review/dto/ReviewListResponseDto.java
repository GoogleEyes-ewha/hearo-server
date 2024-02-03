package com.gdsc.hearo.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewListResponseDto {
    private int reviewCount;
    private List<ReviewDto> reviewList;
}
