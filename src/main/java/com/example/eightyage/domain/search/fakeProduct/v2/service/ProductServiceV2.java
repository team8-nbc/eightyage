package com.example.eightyage.domain.search.fakeProduct.v2.service;

import com.example.eightyage.domain.search.fakeProduct.dto.ProductSearchResponse;
import com.example.eightyage.domain.search.fakeProduct.entity.Category;
import com.example.eightyage.domain.search.fakeProduct.entity.FakeProduct;
import com.example.eightyage.domain.search.fakeProduct.repository.ProductRepository;
import com.example.eightyage.domain.search.v2.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ProductServiceV2 {

    private final ProductRepository productRepository;
    private final SearchService searchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Page<ProductSearchResponse> getProducts(String productName, Category category, int size, int page) {
        int adjustedPage = Math.max(0, page - 1);
        Pageable pageable = PageRequest.of(adjustedPage, size);
        Page<FakeProduct> products = productRepository.findProducts(productName, category, pageable);

        if (StringUtils.hasText(productName) && !products.isEmpty()) {
            searchService.logAndCountKeyword(productName);
        }

        return products.map(ProductSearchResponse::from);
    }
}
