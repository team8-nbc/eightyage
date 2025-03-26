package com.example.eightyage.domain.product.dto.request;

import com.example.eightyage.domain.product.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductSaveRequestDto {

    @NotBlank(message="반드시 값이 있어야 합니다.")
    private String productName;

    @NotNull(message="반드시 값이 있어야 합니다.")
    private Category category;

    @NotBlank(message="반드시 값이 있어야 합니다.")
    private String content;

    @NotNull(message="반드시 값이 있어야 합니다.")
    private Integer price;
}
