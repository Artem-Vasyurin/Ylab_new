package vasyurin.work.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import vasyurin.work.dto.User;
import vasyurin.work.enams.UserRole;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Component
@Profile("!test")
public class AuthorizationFilter implements Filter {

    private static final Map<String, Set<UserRole>> PERMISSIONS = Map.of(
            "POST:/product/get", Set.of(UserRole.USER, UserRole.ADMIN),
            "DELETE:/product/delete", Set.of(UserRole.ADMIN),
            "POST:/product/create", Set.of(UserRole.ADMIN)
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        User user = (User) req.getAttribute("user");

        if (user != null) {
            String key = req.getMethod().toUpperCase() + ":" + req.getRequestURI();
            Set<UserRole> allowedRoles = PERMISSIONS.get(key);

            if (allowedRoles != null && !allowedRoles.contains(user.getRole())) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("{\"error\":\"Нет прав для данного действия\"}");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
