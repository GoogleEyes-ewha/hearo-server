package com.gdsc.hearo.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummaryResponseDto {

    private String positiveSummary;
    private String negativeSummary;

}
