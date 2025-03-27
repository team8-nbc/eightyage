package com.example.eightyage.domain.product.dto.response;

import com.example.eightyage.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ProductSearchResponseDto {
    private final String productName;
    private final String category;
    private final Integer price;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProductSearchResponseDto from(Product product) {
        return ProductSearchResponseDto.builder()
                .productName(product.getName())
                .category(product.getCategory().toString())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getModifiedAt())
                .build();
    }
}
