package com.example.eightyage.domain.review.service;

import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.repository.ProductRepository;
import com.example.eightyage.domain.review.dto.response.ReviewSaveResponseDto;
import com.example.eightyage.domain.review.dto.response.ReviewUpdateResponseDto;
import com.example.eightyage.domain.review.dto.response.ReviewsGetResponseDto;
import com.example.eightyage.domain.review.entity.Review;
import com.example.eightyage.domain.review.repository.ReviewRepository;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    // 리뷰 생성
    @Transactional
    public ReviewSaveResponseDto saveReview(Long userId, Long productId, Double score, String content) {
        User findUser = userService.findUserByIdOrElseThrow(userId);
        Product findProduct = productRepository.findProductByIdOrElseThrow(productId);

        Review review = new Review(findUser, findProduct, score, content);
        Review savedReview = reviewRepository.save(review);

        return new ReviewSaveResponseDto(savedReview.getId(), findUser.getId(), findProduct.getId(), findUser.getNickname(), savedReview.getScore(), savedReview.getContent(), savedReview.getCreatedAt(), savedReview.getModifiedAt());
    }

    // 리뷰 수정
    @Transactional
    public ReviewUpdateResponseDto updateReview(Long userId, Long reviewId, Double score, String content) {
        User findUser = userService.findUserByIdOrElseThrow(userId);
        Review findReview = reviewRepository.findReviewByIdOrElseThrow(reviewId);

        if(findUser.getId() == findReview.getUser().getId()){
            if(content != null) findReview.setContent(content);
            if(score != null) findReview.setScore(score);
        }

        return new ReviewUpdateResponseDto(findReview.getId(), userId, findUser.getNickname(), findReview.getScore(), findReview.getContent(), findReview.getCreatedAt(), findReview.getModifiedAt());
    }

    // 리뷰 다건 조회
    @Transactional(readOnly = true)
    public Page<ReviewsGetResponseDto> findReviews(Long productId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByProductIdAndProductDeletedAtIsNull(productId, pageable);

        return reviewPage.map(review -> new ReviewsGetResponseDto(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getNickname(),
                review.getScore(),
                review.getContent(),
                review.getCreatedAt(),
                review.getModifiedAt()
        ));
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        User findUser = userService.findUserByIdOrElseThrow(userId);
        Review findReview = reviewRepository.findReviewByIdOrElseThrow(reviewId);

        if(findUser.getId() == findReview.getUser().getId()){
            findReview.setDeletedAt(LocalDateTime.now());
        }
    }
}
