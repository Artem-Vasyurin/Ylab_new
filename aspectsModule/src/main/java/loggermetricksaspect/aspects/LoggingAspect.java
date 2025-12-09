package loggermetricksaspect.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Аспект для логирования времени выполнения методов,
 * помеченных {@link loggermetricksaspect.annotations.LoggingServices}.
 */
@Aspect
@Slf4j
public class LoggingAspect {

    /**
     * Логирует время выполнения метода.
     *
     * @param joinPoint вызов метода
     * @return результат выполнения метода
     * @throws Throwable если метод выбросит исключение
     */
    @Around("@annotation(loggermetricksaspect.annotations.LoggingServices)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long time = System.currentTimeMillis() - start;

        String message = joinPoint.getSignature() + " выполнен за " + time + " мс";

        log.trace(message);

        return result;
    }
}
