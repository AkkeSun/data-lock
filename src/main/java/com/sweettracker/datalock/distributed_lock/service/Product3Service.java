package com.sweettracker.datalock.distributed_lock.service;

import com.sweettracker.datalock.distributed_lock.aop.DistributedLock;
import com.sweettracker.datalock.distributed_lock.entity.Product3;
import com.sweettracker.datalock.distributed_lock.repository.Product3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Product3Service {

    private final Product3Repository productRepository;

    @DistributedLock(key = "test")
    @Transactional
    public void decrease(long productId, long quantity) {
        Product3 product = productRepository.findById(productId).get();
        if (product.getQuantity() >= quantity) {
            product.decrease(quantity);
        }
    }
}
