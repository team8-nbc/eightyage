package com.example.eightyage.domain.product.service;

import com.example.eightyage.domain.product.dto.request.ProductSaveRequestDto;
import com.example.eightyage.domain.product.dto.request.ProductUpdateRequestDto;
import com.example.eightyage.domain.product.dto.response.ProductGetResponseDto;
import com.example.eightyage.domain.product.dto.response.ProductSaveResponseDto;
import com.example.eightyage.domain.product.dto.response.ProductUpdateResponseDto;
import com.example.eightyage.domain.product.entity.Category;
import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.entity.SaleState;
import com.example.eightyage.domain.product.repository.ProductRepository;
import com.example.eightyage.domain.review.entity.Review;
import com.example.eightyage.domain.review.repository.ReviewRepository;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ReviewRepository reviewRepository;

    @InjectMocks
    ProductService productService;

    @Mock
    private Product product;

    @Mock
    private Review review1;

    @Mock
    private Review review2;

    @Test
    void 제품_생성_성공(){
        // given
        Product product = new Product(1L, "8자 주름 스킨", Category.SKINCARE, "8자 주름을 1자로 펴주는 퍼펙트 스킨", 20000, SaleState.FOR_SALE);

        given(productRepository.save(any())).willReturn(product);

        ProductSaveRequestDto requestDto = new ProductSaveRequestDto("8자 주름 스킨", Category.SKINCARE, "8자 주름을 1자로 펴줍니다.", 20000);

        // when
        ProductSaveResponseDto savedProduct = productService.saveProduct(requestDto);

        // then
        assertThat(savedProduct.getProductName()).isEqualTo(product.getName());
    }

    @Test
    void 제품_수정_성공(){
        // given
        Long productId = 1L;

        Product product = new Product(1L, "8자 주름 스킨", Category.SKINCARE, "8자 주름을 1자로 펴주는 퍼펙트 스킨", 20000, SaleState.FOR_SALE);

        given(productRepository.findById(any(Long.class))).willReturn(Optional.of(product));

        ProductUpdateRequestDto requestDto = new ProductUpdateRequestDto("8자 주름 향수", Category.FRAGRANCE, "8자 주름의 은은한 향기", SaleState.FOR_SALE, 50000);

        // when
        ProductUpdateResponseDto responseDto = productService.updateProduct(productId, requestDto);

        // then
        assertThat(responseDto.getProductName()).isEqualTo(requestDto.getProductName());
    }

    @Test
    void 제품_단건_조회_성공(){
        // given
        Long productId = 1L;

        given(productRepository.findById(any(Long.class))).willReturn(Optional.of(product));

        // when
        ProductGetResponseDto responseDto = productService.findProductById(productId);

        // then
        assertThat(responseDto.getProductName()).isEqualTo(product.getName());
    }

    @Test
    void 제품_삭제_성공(){
        // given
        Long productId = 1L;

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);

        given(productRepository.findById(any(Long.class))).willReturn(Optional.of(product));
        given(reviewRepository.findReviewsByProductId(any(Long.class))).willReturn(reviewList);

        // when
        productService.deleteProduct(productId);

        // then
        verify(review1, times(1)).delete();
        verify(review2, times(1)).delete();

        verify(product, times(1)).delete();
    }
}