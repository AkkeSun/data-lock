package com.sweettracker.datalock.pessimistic_lock.service;

import com.sweettracker.datalock.pessimistic_lock.entity.Product2;
import com.sweettracker.datalock.pessimistic_lock.reopsitory.Product2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Product2Service {

    private final Product2Repository productRepository;

    @Transactional
    public void decrease(long productId, long quantity) {
        Product2 product = productRepository.findById(productId).get();
        if (product.getQuantity() >= quantity) {
            product.decrease(quantity);
        }
    }
}
