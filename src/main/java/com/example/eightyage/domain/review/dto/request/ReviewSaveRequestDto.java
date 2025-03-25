package com.example.eightyage.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewSaveRequestDto {

    @NotNull(message = "반드시 값이 있어야 합니다.")
    private Double score;

    @NotBlank(message = "반드시 값이 있어야 합니다.")
    private String content;
}
