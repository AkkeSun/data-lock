package com.sweettracker.datalock.distributed_lock.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AopForTransaction {
    /*
        Lock 을 반맙하는 시점에 Database 가 커밋되지 않는다면 데이터 정합성이 깨지게 됩니다.
        따라서 Lock 을 처리하는 작업 내에서는 항상 새로운 트랜젝션을 갖도록 하는 것이 좋습니다.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
