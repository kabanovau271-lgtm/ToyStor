package com.example.ts.aop;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

  @Around("execution(* com.example.ts.service.*.*(..)) || execution(* com.example.ts.controller.*.*(..))")
  public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

    String methodName = joinPoint.getSignature().toShortString();
    Object[] args = joinPoint.getArgs();

    log.debug("Метод {} вызван с параметрами: {}", methodName, Arrays.toString(args));

    long start = System.currentTimeMillis();

    try {
      Object result = joinPoint.proceed();

      long time = System.currentTimeMillis() - start;

      log.info("Метод {} выполнен успешно за {} ms", methodName, time);

      return result;

    } catch (Exception e) {
      long time = System.currentTimeMillis() - start;

      log.error("Метод {} завершился с ошибкой за {} ms: {}", methodName, time, e.getMessage());

      throw e;
    }
  }
}