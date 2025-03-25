package com.example.eightyage.domain.product.dto.response;

import com.example.eightyage.domain.product.entity.Category;
import com.example.eightyage.domain.product.entity.SaleState;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductSaveResponseDto {

    private final String productName;

    private final Category category;

    private final Integer price;

    private final SaleState saleState;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;
}
