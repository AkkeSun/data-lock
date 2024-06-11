package com.sweettracker.datalock.optimistic_lock.aop;

import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.StaleObjectStateException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1) // @Transactional 보다 먼저 실행되도록 설정
public class OptimisticLockRetryAspect {

    @Around("@annotation(retry)")
    public Object retryOptimisticLock(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        Exception exception = null;
        for (int attempt = 0; attempt < retry.maxRetries(); attempt++) {
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException |
                     StaleObjectStateException e) {
                log.info("데이터 충돌 발생");
                exception = e;
                Thread.sleep(retry.retryDelay());
            }
        }
        throw exception;
    }
}
