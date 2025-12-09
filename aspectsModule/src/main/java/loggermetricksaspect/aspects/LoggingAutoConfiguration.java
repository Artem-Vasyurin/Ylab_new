package loggermetricksaspect.aspects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Конфигурация для автоматического подключения аспекта логирования.
 * <p>Создаёт бин {@link LoggingAspect} и включает поддержку Spring AOP.</p>
 */
@Configuration
@EnableAspectJAutoProxy
public class LoggingAutoConfiguration {
    /**
     * Создаёт бин {@link LoggingAspect} для логирования времени выполнения методов.
     *
     * @return новый экземпляр LoggingAspect
     */
    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}