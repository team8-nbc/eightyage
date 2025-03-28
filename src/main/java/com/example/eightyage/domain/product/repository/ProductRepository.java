package com.example.eightyage.domain.product.repository;

import com.example.eightyage.domain.product.entity.Category;
import com.example.eightyage.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.id = :productId AND p.deletedAt IS NULL")
    Optional<Product> findById(@Param("productId") Long productId);

    @Query("SELECT p FROM Product p WHERE p.saleState = 'FOR_SALE' " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND (:name IS NULL OR p.name LIKE CONCAT('%', :name, '%')) " +
            "ORDER BY p.name")
    Page<Product> findProducts(
            @Param("name")String name,
            @Param("category") Category category,
            Pageable pageable
    );
}
