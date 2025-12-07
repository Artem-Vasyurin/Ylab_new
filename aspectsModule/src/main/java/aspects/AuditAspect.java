//package aspects;
//
//import annotations.Auditing;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//import vasyurin.work.dto.User;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.util.Arrays;
//
//@Aspect
//@Slf4j
//public class AuditAspect {
//
//    @After("@annotation(auditing)")
//    public void audit(JoinPoint jp, Auditing auditing) {
//        String method = jp.getSignature().toShortString();
//        Object[] args = jp.getArgs();
//
//        // Получаем HttpServletRequest через RequestContextHolder
//        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (attrs == null) {
//            log.trace("Нет контекста запроса для аудита: " + method);
//            return;
//        }
//
//        HttpServletRequest request = attrs.getRequest();
//        User user = (User) request.getSession().getAttribute("user");
//
//        String username = user != null ? user.getUsername() : "SYSTEM";
//
//        log.trace("Пользователь: {}, метод: {}, параметры: {}", username, method, safeArgs(args));
//
//        // TODO: сюда можно вызвать auditService.log(user, ...)
//    }
//
//    private String safeArgs(Object[] args) {
//        return Arrays.stream(args)
//                .map(arg -> {
//                    if (arg instanceof User u) {
//                        return "{username=" + u.getUsername() + "}";
//                    }
//                    return String.valueOf(arg);
//                })
//                .toList()
//                .toString();
//    }
//}
