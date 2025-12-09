package vasyurin.work.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import vasyurin.work.dto.User;
import vasyurin.work.services.interfases.SecurityService;

import java.io.IOException;

/**
 * Фильтр аутентификации, проверяющий наличие и валидность JWT-токена.
 * <p>
 * Пропускает:
 * <ul>
 *     <li>Запросы с валидным токеном, добавляя объект User в атрибуты запроса.</li>
 *     <li>Публичные эндпоинты, такие, как документация, логин и получение продуктов.</li>
 * </ul>
 * Если токен отсутствует или недействителен, возвращает статус 401 (UNAUTHORIZED).
 */
@Component
@Profile("!test")
public class AuthenticationFilter implements Filter {

    private final SecurityService securityService;

    public AuthenticationFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * Выполняет проверку токена в заголовке Authorization.
     * <p>
     * Логика:
     * <ol>
     *     <li>Если токен валиден, добавляет пользователя в атрибут запроса и передаёт управление дальше.</li>
     *     <li>Если путь публичный, передаёт управление дальше без проверки токена.</li>
     *     <li>Иначе возвращает 401 и сообщение об ошибке.</li>
     * </ol>
     *
     * @param request  HTTP-запрос
     * @param response HTTP-ответ
     * @param chain    цепочка фильтров
     * @throws IOException      при ошибках ввода-вывода
     * @throws ServletException при ошибках фильтрации
     */
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
