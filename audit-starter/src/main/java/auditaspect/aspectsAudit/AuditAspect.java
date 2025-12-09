package auditaspect.aspectsAudit;

import auditaspect.services.AuditLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Аспект для аудита действий пользователей в приложении.
 * <p>
 * Основные функции:
 * <ul>
 *     <li>Определяет пользователя, выполняющего метод, через аргументы метода или атрибут {@code "user"} в HTTP-запросе.</li>
 *     <li>Логирует действия создания и удаления объектов с помощью {@link AuditLogger}.</li>
 *     <li>Работает только на методах, помеченных аннотацией {@code @Auditing}.</li>
 * </ul>
 * <p>
 * Пример работы:
 * <pre>
 * {@code
 * @Auditing
 * public void createProduct(Product product) { ... }
 * }
 * </pre>
 * <p>
 * Пользователь определяется следующим образом:
 * <ol>
 *     <li>Если в аргументах метода есть объект, реализующий {@link AuditableUser}, берется его username.</li>
 *     <li>Если нет, ищется объект пользователя в HTTP-сессии (атрибут {@code "user"}).</li>
 *     <li>Если пользователь не найден, используется константа {@link #ANONYMOUS}.</li>
 * </ol>
 */
@Aspect
@Slf4j
public class AuditAspect {

    public static final String ANONYMOUS = "ANONYMOUS";
    private final AuditLogger auditLogger;

    public AuditAspect(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    /**
     * После выполнения метода, помеченного {@code @Auditing}, логирует действия пользователя.
     * <p>
     * Разделяет действия на:
     * <ul>
     *     <li>{@code create} — логирует создание/изменение объекта</li>
     *     <li>{@code delete} — логирует удаление объекта</li>
     * </ul>
     *
     * @param jp информация о выполняемом методе и его аргументах
     */
    @After("@annotation(auditaspect.annotationsAudit.Auditing)")
    public void audit(JoinPoint jp) {
        String username = getUsername(jp);
        String methodName = jp.getSignature().getName();

        log.trace("Аудит: пользователь = {}, метод = {}", username, jp.getSignature());

        for (Object arg : jp.getArgs()) {
            if (arg instanceof AuditableUser) continue;

            if ("create".equals(methodName)) {
                auditLogger.logCreate(arg);
            } else if ("delete".equals(methodName)) {
                auditLogger.logDelete(arg);
            }
        }
    }

    private String getUsername(JoinPoint jp) {
        for (Object arg : jp.getArgs()) {
            if (arg instanceof AuditableUser u) {
                return u.getUsername();
            }
        }

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            Object sessionUser = request.getAttribute("user");
            if (sessionUser instanceof AuditableUser u) {
                return u.getUsername();
            }
        }
        return ANONYMOUS;
    }
}