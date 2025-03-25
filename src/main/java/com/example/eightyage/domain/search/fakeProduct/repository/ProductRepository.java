package com.example.eightyage.domain.search.fakeProduct.repository;

import com.example.eightyage.domain.search.fakeProduct.entity.Category;
import com.example.eightyage.domain.search.fakeProduct.entity.FakeProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<FakeProduct, Long> {

    @Query("SELECT p FROM FakeProduct p WHERE p.saleState = 'ON_SALE' " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND (:name IS NULL OR p.name LIKE CONCAT('%', :name, '%')) " +
            "ORDER BY p.name")
    Page<FakeProduct> findProducts(
            @Param("name")String name,
            @Param("category") Category category,
            Pageable pageable
    );

}