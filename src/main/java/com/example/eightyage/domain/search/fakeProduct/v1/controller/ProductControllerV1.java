package com.example.eightyage.domain.search.fakeProduct.v1.controller;

import com.example.eightyage.domain.search.fakeProduct.dto.ProductSearchResponse;
import com.example.eightyage.domain.search.fakeProduct.entity.Category;
import com.example.eightyage.domain.search.fakeProduct.v1.service.ProductServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductControllerV1 {

    private final ProductServiceV1 productService;

    @GetMapping("/api/v1/products")
    public ResponseEntity<Page<ProductSearchResponse>> searchProduct(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(productService.getProducts(name, category, size, page));
    }
}