package com.example.eightyage.domain.product.controller;

import com.example.eightyage.domain.product.dto.request.ProductSaveRequestDto;
import com.example.eightyage.domain.product.dto.request.ProductUpdateRequestDto;
import com.example.eightyage.domain.product.dto.response.ProductGetResponseDto;
import com.example.eightyage.domain.product.dto.response.ProductSaveResponseDto;
import com.example.eightyage.domain.product.dto.response.ProductUpdateResponseDto;
import com.example.eightyage.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 제품 생성
    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<Void> saveProduct(@Valid @RequestBody ProductSaveRequestDto requestDto){
        productService.saveProduct(requestDto.getProductName(), requestDto.getCategory(), requestDto.getContent(), requestDto.getPrice());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 제품 수정
    @Secured("ROLE_ADMIN")
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductUpdateResponseDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequestDto requestDto
    ){
        ProductUpdateResponseDto responseDto = productService.updateProduct(productId, requestDto.getProductName(), requestDto.getCategory(), requestDto.getContent(), requestDto.getSaleState(), requestDto.getPrice());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 제품 단건 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductGetResponseDto> getProduct(@PathVariable Long productId){
        ProductGetResponseDto responseDto = productService.findProductById(productId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 제품 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
