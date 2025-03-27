package com.example.eightyage.bulk;

import com.example.eightyage.domain.review.entity.Review;
import com.example.eightyage.domain.review.repository.ReviewBulkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Profile(value = "test")
public class ReviewBulkTest {

    @Autowired
    private ReviewBulkRepository reviewBulkRepository;

    @Test
    void 리뷰_데이터_오백만건_생성() {

        List<Review> batchList = new ArrayList<>();

        for (int i = 0; i < 5_000_000; i++) {
            Review review = new Review();
            batchList.add(review);

            if (batchList.size() == 5000) {
                reviewBulkRepository.bulkInsertReviews(batchList);
                batchList.clear();

                sleep(500);
            }
        }

        if (!batchList.isEmpty()) {
            reviewBulkRepository.bulkInsertReviews(batchList);
            batchList.clear();
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
