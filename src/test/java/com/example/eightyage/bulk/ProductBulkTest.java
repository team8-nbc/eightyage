package com.example.eightyage.bulk;

import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.repository.ProductBulkRepository;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.entity.UserRole;
import com.example.eightyage.domain.user.repository.UserBulkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Profile(value = "test")
public class ProductBulkTest {

    @Autowired
    private ProductBulkRepository productBulkRepository;

    @Test
    void 제품_데이터_백만건_생성() {

        List<Product> batchList = new ArrayList<>();

        for (int i = 0; i < 1_000; i++) {
            Product product = Product.builder()
                    .name(UUID.randomUUID().toString())
                    .build();
            batchList.add(product);

            if (batchList.size() == 50) {
                productBulkRepository.bulkInsertProduct(batchList);
                batchList.clear();

//                sleep(500);
            }
        }

        if (!batchList.isEmpty()) {
            productBulkRepository.bulkInsertProduct(batchList);
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
