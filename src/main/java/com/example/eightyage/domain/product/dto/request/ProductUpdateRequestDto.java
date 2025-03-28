package com.example.eightyage.domain.product.dto.request;

import com.example.eightyage.domain.product.entity.Category;
import com.example.eightyage.domain.product.entity.SaleState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductUpdateRequestDto {

    private String productName;

    private Category category;

    private String content;

    private SaleState saleState;

    private Integer price;
}
