package com.example.eightyage.domain.review.repository;

import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.review.entity.Review;
import com.example.eightyage.global.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.id = :reviewId AND r.deletedAt IS NULL")
    Optional<Review> findById(@Param("reviewId") Long reviewId);

    default Review findReviewByIdOrElseThrow(Long reviewId){
        return findById(reviewId).orElseThrow(() -> new NotFoundException("해당 리뷰가 존재하지 않습니다."));
    }

    Page<Review> findByProductIdAndProductDeletedAtIsNull(Long productId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND r.deletedAt IS NULL")
    List<Review> findReviewsByProductId(@Param("productId") Long productId);
}
