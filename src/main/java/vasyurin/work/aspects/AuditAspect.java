package vasyurin.work.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import vasyurin.work.dto.User;
import vasyurin.work.dto.UserContext;
import vasyurin.work.services.AuditService;

import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @After("@annotation(vasyurin.work.annotations.AuditAction)")
    public void audit(JoinPoint jp) {
        String method = jp.getSignature().getName();
        Object[] args = jp.getArgs();

        User user = UserContext.get();

        if ("login".equals(method)) {
            User loginReq = (args.length > 0 && args[0] instanceof User u) ? u : null;
            String username = loginReq != null ? loginReq.getUsername() : "unknown";

            auditService.log("Пользователь " + username + " пытается войти");
            return;
        }

        if (user != null) {
            auditService.log(user, "Вызов метода: " + method + " параметры: " + safeArgs(args));
        } else {
            auditService.log("Системное действие: " + method + " параметры: " + safeArgs(args));
        }
    }

    private String safeArgs(Object[] args) {
        return Arrays.stream(args)
                .map(arg -> {
                    if (arg instanceof User u) {
                        return "{username=" + u.getUsername() + "}";
                    }
                    return String.valueOf(arg);
                })
                .toList()
                .toString();
    }
}
