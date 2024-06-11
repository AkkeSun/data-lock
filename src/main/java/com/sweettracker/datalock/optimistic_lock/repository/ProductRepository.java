package com.sweettracker.datalock.optimistic_lock.repository;

import com.sweettracker.datalock.optimistic_lock.entity.Product;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /*
        NONE
            - @Version 을 명시한 컬럼이 있다면 자동으로 설정된다.
            - 데이터를 수정할 때 버전이 증가한다.
        OPTIMISTIC:
            - 엔티티를 조회만 해도 버전이 증가한다.
            - 트랜잭션을 종료할 때까지 다른 트랜잭션에서 변경하지 않음을 보장한다.
        OPTIMISTIC_FORCE_INCREMENT
            - 낙관적 락을 사용하면서 버전 정보를 강제로 증가시킨다.
            - 물리적으로 변경되지 않았지만 논리적으로 변경되었을 경우 버전을 증가하고 싶을 때 사용한다 (연관관계 매핑)
     */
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Product> findById(long id);
}
