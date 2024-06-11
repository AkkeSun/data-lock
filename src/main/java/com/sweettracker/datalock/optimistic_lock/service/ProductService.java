package com.sweettracker.datalock.optimistic_lock.service;

import com.sweettracker.datalock.optimistic_lock.aop.Retry;
import com.sweettracker.datalock.optimistic_lock.entity.Product;
import com.sweettracker.datalock.optimistic_lock.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Retry
    @Transactional
    public void decrease(long productId, long quantity) {
        Product product = productRepository.findById(productId).get();
        if (product.getQuantity() >= quantity) {
            product.decrease(quantity);
        }
    }
}
