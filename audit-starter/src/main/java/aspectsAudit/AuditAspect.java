package aspectsAudit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Slf4j
public class AuditAspect {

    public static final String ANONYMOUS = "ANONYMOUS";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @After("@annotation(annotationsAudit.Auditing)")
    public void audit(JoinPoint jp) {

        String username = ANONYMOUS;

        for (Object arg : jp.getArgs()) {
            if (arg instanceof AuditableUser u) {
                username = u.getUsername();
                break;
            }
        }

        if (ANONYMOUS.equals(username)) {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                Object sessionUser = request.getAttribute("user");
                if (sessionUser instanceof AuditableUser u) {
                    username = u.getUsername();
                }
            }
        }

        log.trace("Аудит: пользователь = {}, метод = {}", username, jp.getSignature());

        String methodName = jp.getSignature().getName();

        for (Object arg : jp.getArgs()) {
            if (arg instanceof AuditableUser) continue;

            try {
                if ("create".equals(methodName)) {
                    String json = objectMapper.writeValueAsString(arg);
                    log.trace("Аудит: объект = {}", json);
                } else if ("delete".equals(methodName)) {
                    String gtin = null;
                    try {
                        Object gtinObj = arg.getClass().getMethod("getGtin").invoke(arg);
                        if (gtinObj != null) {
                            gtin = gtinObj.toString();
                        }
                    } catch (Exception e) {
                        log.warn("Не удалось получить gtin из объекта для аудита", e);
                    }
                    if (gtin != null) {
                        log.trace("Аудит: удаляемый объект gtin = {}", gtin);
                    }
                }

            } catch (JsonProcessingException e) {
                log.warn("Не удалось сериализовать объект для аудита", e);
            }
        }
    }
}
