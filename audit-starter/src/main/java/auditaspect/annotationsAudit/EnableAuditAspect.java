package auditaspect.annotationsAudit;

import auditaspect.aspectsAudit.AuditAspectConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Включает аспекты аудита в Spring-контексте.
 * <p>Импортирует {@link AuditAspectConfiguration} для автоматической регистрации бинов аудита.</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AuditAspectConfiguration.class)
public @interface EnableAuditAspect {
}