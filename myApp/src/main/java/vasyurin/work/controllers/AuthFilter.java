package vasyurin.work.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import vasyurin.work.dto.User;
import vasyurin.work.enams.UserRole;
import vasyurin.work.services.security.SecurityService;

import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    SecurityService securityService;

    public AuthFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-ui") ||
            path.startsWith("/login") ||
            path.startsWith("/hello") ||
            path.startsWith("/product/get") ||
            req.getMethod().equalsIgnoreCase("GET")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");
        User user = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            user = securityService.parseToken(token).orElse(null);
        }

        if (user == null) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                user = (User) session.getAttribute("user");
            }
        }

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Не авторизован\"}");
            return;
        }

        if ((req.getMethod().equalsIgnoreCase("POST") || req.getMethod().equalsIgnoreCase("DELETE"))
            && user.getRole() != UserRole.ADMIN) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"Нет прав для доступа\"}");
            return;
        }

        req.getSession().setAttribute("user", user);

        chain.doFilter(request, response);
    }
}
