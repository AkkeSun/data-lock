package com.sweettracker.datalock.distributed_lock.repository;

import com.sweettracker.datalock.distributed_lock.entity.Product3;
import com.sweettracker.datalock.optimistic_lock.entity.Product;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface Product3Repository extends JpaRepository<Product3, Long> {
}
