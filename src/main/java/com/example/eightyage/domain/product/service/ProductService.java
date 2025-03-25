package com.example.eightyage.domain.product.service;

import com.example.eightyage.domain.product.dto.response.ProductGetResponseDto;
import com.example.eightyage.domain.product.dto.response.ProductSaveResponseDto;
import com.example.eightyage.domain.product.dto.response.ProductUpdateResponseDto;
import com.example.eightyage.domain.product.entity.Category;
import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.entity.SaleState;
import com.example.eightyage.domain.product.repository.ProductRepository;
import com.example.eightyage.global.exception.NotFoundException;
import com.example.eightyage.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 제품 생성
    @Transactional
    public void saveProduct(String productName, Category category, String content, Integer price) {
        Product product = new Product(productName, category, content, price, SaleState.FOR_SALE);

        productRepository.save(product);
    }

    // 제품 수정
    @Transactional
    public ProductUpdateResponseDto updateProduct(Long productId, String productName, Category category, String content, SaleState saleState, Integer price) {
        Product findProduct = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 상품입니다.")
        );

        if(productName != null) findProduct.setName(productName);
        if(category != null) findProduct.setCategory(category);
        if(content != null) findProduct.setContent(content);
        if(saleState != null) findProduct.setSaleState(saleState);
        if(price != null) findProduct.setPrice(price);

        return new ProductUpdateResponseDto(findProduct.getName(), findProduct.getPrice(), findProduct.getContent(), findProduct.getCategory(), findProduct.getSaleState(), findProduct.getCreatedAt(), findProduct.getModifiedAt());
    }

    // 제품 단건 조회
    @Transactional(readOnly = true)
    public ProductGetResponseDto findProductById(Long productId) {
        Product findProduct = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 상품입니다.")
        );

        return new ProductGetResponseDto(findProduct.getName(), findProduct.getContent(), findProduct.getCategory(), findProduct.getPrice(), findProduct.getSaleState(), findProduct.getCreatedAt(), findProduct.getModifiedAt());
    }

    // 제품 삭제
    @Transactional
    public void deleteProduct(Long productId) {
        Product findProduct = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 상품입니다.")
        );

        findProduct.setDeletedAt(LocalDateTime.now());
    }
}
