package auditaspect.aspectsAudit;

import auditaspect.services.AuditLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Конфигурация аспектов аудита.
 * <p>Создаёт бины для {@link AuditLogger} и {@link AuditAspect} с поддержкой Spring AOP.</p>
 */
@Configuration
@EnableAspectJAutoProxy
public class AuditAspectConfiguration {

    /**
     * Создаёт бин {@link AuditLogger} для логирования аудита.
     *
     * @return новый экземпляр AuditLogger
     */
    @Bean
    public AuditLogger auditLogger() {
        return new AuditLogger(new ObjectMapper());
    }

    /**
     * Создаёт бин {@link AuditAspect} для аудита вызовов методов.
     *
     * @param auditLogger бин AuditLogger
     * @return новый экземпляр AuditAspect
     */
    @Bean
    public AuditAspect auditAspect(AuditLogger auditLogger) {
        return new AuditAspect(auditLogger);
    }
}