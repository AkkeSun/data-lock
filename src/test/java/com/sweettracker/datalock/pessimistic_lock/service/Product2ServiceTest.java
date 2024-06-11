package com.sweettracker.datalock.pessimistic_lock.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sweettracker.datalock.optimistic_lock.entity.Product;
import com.sweettracker.datalock.optimistic_lock.repository.ProductRepository;
import com.sweettracker.datalock.optimistic_lock.service.ProductService;
import com.sweettracker.datalock.pessimistic_lock.entity.Product2;
import com.sweettracker.datalock.pessimistic_lock.reopsitory.Product2Repository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Product2ServiceTest {

    @Autowired
    Product2Service productService;

    @Autowired
    Product2Repository productRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("[success] 데이터 충돌시 로직을 재시도하여 정상적으로 정보가 수정되는지 확인한다.")
    void test() throws InterruptedException {

        // given
        Product2 product = Product2.builder()
            .name("상품1")
            .version(0L)
            .quantity(100)
            .build();
        Product2 savedData = productRepository.save(product);

        int count = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(count);

        //  when
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                try {
                    productService.decrease(savedData.getId(), 10);
                } catch (Exception e) {
                    e.getStackTrace();
                }
                latch.countDown();
            });
        }

        latch.await();
        long quantity = productRepository.findById(savedData.getId()).get().getQuantity();
        System.out.println("quantity: " + quantity);

        // then
        assertThat(quantity).isEqualTo(0);
    }

}