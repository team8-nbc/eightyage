package com.example.eightyage.domain.product.repository;

import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.review.entity.Review;
import com.example.eightyage.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.id = :productId AND p.deletedAt IS NULL")
    Optional<Product> findById(@Param("productId") Long productId);

    default Product findProductByIdOrElseThrow(Long productId){
        return findById(productId).orElseThrow(() -> new NotFoundException("해당 제품이 존재하지 않습니다."));
    }
}
