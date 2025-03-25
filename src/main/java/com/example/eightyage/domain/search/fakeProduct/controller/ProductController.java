package com.example.eightyage.domain.search.fakeProduct.controller;

import com.example.eightyage.domain.search.fakeProduct.dto.ProductSearchResponse;
import com.example.eightyage.domain.search.fakeProduct.entity.Category;
import com.example.eightyage.domain.search.fakeProduct.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

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