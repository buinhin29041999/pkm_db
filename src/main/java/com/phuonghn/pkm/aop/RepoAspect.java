package com.phuonghn.pkm.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class RepoAspect {

    @Around("execution(* com.phuonghn.pkm.repository.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        String message = joinPoint.getSignature() + " exec in " + executionTime + " ms";
        int executionLimitMs = 1000;
        if (executionTime >= executionLimitMs) {
            log.debug("======================================");
            log.debug("{} {}", message, " : SLOW QUERY");
            log.debug("======================================");

        }
        return proceed;
    }
}
