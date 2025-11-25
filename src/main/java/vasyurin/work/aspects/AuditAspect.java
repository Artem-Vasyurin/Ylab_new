package vasyurin.work.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import vasyurin.work.dto.User;
import vasyurin.work.dto.UserContext;
import vasyurin.work.services.AuditServiceImpl;

import java.util.Arrays;

@Aspect
public class AuditAspect {

    @After("@annotation(vasyurin.work.annotations.AuditAction)")
    public void audit(JoinPoint jp) {
        String method = jp.getSignature().getName();
        Object[] args = jp.getArgs();

        User user = UserContext.get();

        // ---- 1. LOGIN ----
        if (method.equals("login")) {

            User loginReq = (args.length > 0 && args[0] instanceof User u) ? u : null;
            String username = loginReq != null ? loginReq.getUsername() : "unknown";

            AuditServiceImpl.getInstance().log(
                    "Пользователь " + username + " пытается войти"
            );
            return;
        }

        // ---- 2. ВСЕ ОСТАЛЬНЫЕ МЕТОДЫ ----
        if (user != null) {
            AuditServiceImpl.getInstance().log(
                    user,
                    "Вызов метода: " + method + " параметры: " + safeArgs(args)
            );
        } else {
            AuditServiceImpl.getInstance().log(
                    "Системное действие: " + method + " параметры: " + safeArgs(args)
            );
        }
    }

    private String safeArgs(Object[] args) {
        return Arrays.stream(args)
                .map(arg -> {
                    if (arg instanceof User u) {
                        // НЕ ЛОГИРУЕМ ПАРОЛЬ/ТОКЕН/РОЛЬ!
                        return "{username=" + u.getUsername() + "}";
                    }
                    return String.valueOf(arg);
                })
                .toList()
                .toString();
    }
}
