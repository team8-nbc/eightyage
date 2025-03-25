package com.example.eightyage.domain.search.fakeProduct.dto;

import com.example.eightyage.domain.search.fakeProduct.entity.FakeProduct;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ProductSearchResponse {
    private final String productName;
    private final String category;
    private final Long price;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProductSearchResponse from(FakeProduct fakeProduct) {
        return ProductSearchResponse.builder()
                .productName(fakeProduct.getName())
                .category(fakeProduct.getCategory().toString())
                .price(fakeProduct.getPrice())
                .createdAt(fakeProduct.getCreatedAt())
                .updatedAt(fakeProduct.getUpdatedAt())
                .build();
    }
}
