package com.example.eightyage.domain.product.service;

import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.entity.ProductImage;
import com.example.eightyage.domain.product.repository.ProductImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Optional;
import java.util.function.Consumer;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Mock
    ProductImage productImage;

    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp(){
        mockFile = new MockMultipartFile(
                "file",
                "tesst.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        ReflectionTestUtils.setField(productImageService, "bucket", "test-bucket");
        ReflectionTestUtils.setField(productImageService, "region", "us-west-2");
    }

    @Test
    void 이미지_업로드_성공(){
        // given
        Long productId = 1L;
        String bucket = "test-bucket";
        String region = "us-west-2";
        String expectedImageUrl = String.format("https://%s.s3.%s.amazonaws.com/", bucket, region);

        given(productImageRepository.save(any())).willReturn(productImage);

        // when
        String imageUrl = productImageService.uploadImage(productId, mockFile);

        // then
        assertTrue(imageUrl.startsWith(expectedImageUrl));
    }

    @Test
    void 이미지_삭제_성공(){
        // given
        Long imageId = 1L;
        String imageUrl = "imageUrl-example";

        given(productImageRepository.findById(any())).willReturn(Optional.of(productImage));

        // when
        productImageService.deleteImage(imageId);

        // then
        verify(productImage, times(1)).delete();
    }
}