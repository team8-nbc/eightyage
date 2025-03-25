package com.example.eightyage.domain.search.fakeProduct.service;

import com.example.eightyage.domain.search.fakeProduct.dto.ProductSearchResponse;
import com.example.eightyage.domain.search.fakeProduct.entity.Category;
import com.example.eightyage.domain.search.fakeProduct.entity.FakeProduct;
import com.example.eightyage.domain.search.fakeProduct.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductSearchResponse> getProducts(String productName, Category category, int size, int page) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustedPage, size);
        Page<FakeProduct> products = productRepository.findProducts(productName, category, pageable);
        return products.map(ProductSearchResponse::from);
    }
}