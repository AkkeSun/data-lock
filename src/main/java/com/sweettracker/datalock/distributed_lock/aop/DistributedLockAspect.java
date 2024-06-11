package com.sweettracker.datalock.distributed_lock.aop;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1) // @Transactional 보다 먼저 실행되도록 설정
@RequiredArgsConstructor
public class DistributedLockAspect {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";
    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(distributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String key = distributedLock.isUniqueKey() ?
            makeUniqueKey(distributedLock, joinPoint.getArgs(), method) :
            REDISSON_LOCK_PREFIX + distributedLock.key();
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!available) {
                return false;
            }
            return aopForTransaction.proceed(joinPoint);

        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock {} {}", method.getName(), key);
            }
        }
    }

    public String makeUniqueKey(DistributedLock distributedLock, Object[] args, Method method) {
        StringBuffer sb = new StringBuffer();
        sb.append(REDISSON_LOCK_PREFIX)
            .append(distributedLock.key())
            .append(method.getName());
        for (Object arg : args) {
            sb.append(arg);
        }

        return sb.toString();
    }
}
