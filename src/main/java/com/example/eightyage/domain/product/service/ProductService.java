package com.example.eightyage.domain.product.service;

import com.example.eightyage.domain.product.dto.response.ProductGetResponseDto;
import com.example.eightyage.domain.product.dto.response.ProductSaveResponseDto;
import com.example.eightyage.domain.product.dto.response.ProductUpdateResponseDto;
import com.example.eightyage.domain.product.entity.Category;
import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.entity.SaleState;
import com.example.eightyage.domain.product.repository.ProductRepository;
import com.example.eightyage.domain.review.entity.Review;
import com.example.eightyage.domain.review.repository.ReviewRepository;
import com.example.eightyage.global.exception.NotFoundException;
import com.example.eightyage.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    // 제품 생성
    @Transactional
    public ProductSaveResponseDto saveProduct(String productName, Category category, String content, Integer price) {
        Product product = new Product(productName, category, content, price, SaleState.FOR_SALE);

        Product savedProduct = productRepository.save(product);

        return new ProductSaveResponseDto(savedProduct.getName(), savedProduct.getCategory(), savedProduct.getPrice(), savedProduct.getSaleState(), savedProduct.getCreatedAt(), savedProduct.getModifiedAt());
    }

    // 제품 수정
    @Transactional
    public ProductUpdateResponseDto updateProduct(Long productId, String productName, Category category, String content, SaleState saleState, Integer price) {
        Product findProduct = productRepository.findProductByIdOrElseThrow(productId);

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
        Product findProduct = productRepository.findProductByIdOrElseThrow(productId);

        return new ProductGetResponseDto(findProduct.getName(), findProduct.getContent(), findProduct.getCategory(), findProduct.getPrice(), findProduct.getSaleState(), findProduct.getCreatedAt(), findProduct.getModifiedAt());
    }

    // 제품 삭제
    @Transactional
    public void deleteProduct(Long productId) {
        Product findProduct = productRepository.findProductByIdOrElseThrow(productId);
        List<Review> findReviewList = reviewRepository.findReviewsByProductId(productId);

        for(Review review : findReviewList){
            review.setDeletedAt(LocalDateTime.now());
        }

        findProduct.setDeletedAt(LocalDateTime.now());
    }
}
