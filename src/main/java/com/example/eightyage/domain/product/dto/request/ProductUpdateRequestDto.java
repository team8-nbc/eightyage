package com.example.eightyage.domain.product.dto.request;

import com.example.eightyage.domain.product.entity.Category;
import com.example.eightyage.domain.product.entity.SaleState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductUpdateRequestDto {

    @NotBlank(message="반드시 값이 있어야 합니다.")
    private String productName;

    @NotBlank(message="반드시 값이 있어야 합니다.")
    private Category category;

    @NotBlank(message="반드시 값이 있어야 합니다.")
    private String content;

    @NotBlank(message="반드시 값이 있어야 합니다.")
    private SaleState saleState;

    @NotNull(message="반드시 값이 있어야 합니다.")
    private Integer price;
}
