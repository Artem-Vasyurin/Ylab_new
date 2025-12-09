package annotationsAudit;

import aspectsAudit.AuditAspectConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AuditAspectConfiguration.class)
public @interface EnableAuditAspect {
}
