package com.example.eightyage.domain.product.service;

import com.example.eightyage.domain.product.repository.ProductImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductImageServiceTest {

    @Mock
    S3Client s3Client;

    @Mock
    ProductImageRepository productImageRepository;

    @Mock
    ProductService productService;

    @InjectMocks
    ProductImageService productImageService;

    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp(){
        mockFile = new MockMultipartFile(
                "file",
                "tesst.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    @Test
    void 이미지_업로드_성공(){

    }
}