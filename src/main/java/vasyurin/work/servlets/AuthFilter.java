package vasyurin.work.servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vasyurin.work.dto.User;
import vasyurin.work.dto.UserContext;
import vasyurin.work.services.security.SecurityServiceImpl;

import java.io.IOException;


@WebFilter(urlPatterns = "/product/*")
public class AuthFilter implements Filter {

    private final SecurityServiceImpl securityService;

    private AuthFilter() {
        this.securityService = SecurityServiceImpl.getInstance();
    }

    public AuthFilter(SecurityServiceImpl securityServiceMock) {
        this.securityService = securityServiceMock;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String token = req.getHeader("X-Auth-Token");
        if (token == null || token.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Отсутствует токен\"}");
            return;
        }

        var userOpt = securityService.getByToken(token);
        if (userOpt.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Неверный токен\"}");
            return;
        }

        User user = userOpt.get();
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"Нет прав для доступа\"}");
            return;
        }

        req.setAttribute("user", user);
        UserContext.set(user);
        try {
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
