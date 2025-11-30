package vasyurin.work.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import vasyurin.work.services.LoggingService;

@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* vasyurin.work.services..*(..)) && !execution(* vasyurin.work.services.LoggingService.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long time = System.currentTimeMillis() - start;

        String message = joinPoint.getSignature() + " выполнен за " + time + " мс";
        System.out.println(message);
        LoggingService.logExecution(message);

        return result;
    }
}
