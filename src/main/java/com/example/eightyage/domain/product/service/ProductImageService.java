package com.example.eightyage.domain.product.service;

import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.entity.ProductImage;
import com.example.eightyage.domain.product.repository.ProductImageRepository;
import com.example.eightyage.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final S3Client s3Client;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.region}")
    private String region;

    // 제품 이미지 업로드
    @Transactional
    public String uploadImage(Long productId, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 파일명 중복 방지

        // S3에 업로드
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        // S3 이미지 URL 생성
        String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);

        // DB 저장
        Product product = productRepository.findProductByIdOrElseThrow(productId);
        ProductImage productImage = new ProductImage(product, imageUrl);
        productImageRepository.save(productImage);

        return imageUrl;
    }

    // 제품 이미지 삭제
    @Transactional
    public void deleteImage(Long imageId) {
        ProductImage findProductImage = productImageRepository.findProductImageByIdOrElseThrow(imageId);

        findProductImage.setDeletedAt(LocalDateTime.now());
    }
}

