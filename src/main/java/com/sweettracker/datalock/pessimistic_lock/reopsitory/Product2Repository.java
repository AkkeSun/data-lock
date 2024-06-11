package com.sweettracker.datalock.pessimistic_lock.reopsitory;

import com.sweettracker.datalock.pessimistic_lock.entity.Product2;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface Product2Repository extends JpaRepository<Product2, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product2> findById(long id);
}
