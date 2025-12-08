package vasyurin.work.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import vasyurin.work.dto.User;
import vasyurin.work.services.interfases.SecurityService;

import java.io.IOException;

@Component
@Profile("!test")
public class AuthenticationFilter implements Filter {

    private final SecurityService securityService;

    public AuthenticationFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI();

        boolean isPublic = path.startsWith("/v3/api-docs")
                           || path.startsWith("/swagger-ui")
                           || path.startsWith("/hello")
                           || path.startsWith("/product/get")
                           || path.startsWith("/login");


        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            User user = securityService.parseToken(token).orElse(null);
            if (user != null) {
                req.setAttribute("user", user);
                chain.doFilter(request, response);
                return;
            }
        }

        if (isPublic) {
            chain.doFilter(request, response);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.getWriter().write("{\"error\":\"Нет токена или токен неверный\"}");
    }
}
