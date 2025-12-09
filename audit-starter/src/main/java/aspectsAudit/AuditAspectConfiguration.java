package aspectsAudit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AuditAspectConfiguration {

    @Bean
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }
}
