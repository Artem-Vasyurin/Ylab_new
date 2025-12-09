package vasyurin.work.controllers;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import vasyurin.work.dto.User;
import vasyurin.work.services.interfases.SecurityService;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthenticationFilterTest {

    private SecurityService securityService;
    private AuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        securityService = mock(SecurityService.class);
        filter = new AuthenticationFilter(securityService);
    }

    @Test
    @DisplayName("Успешная аутентификация с валидным токеном")
    void successfulAuthentication() throws ServletException, IOException {
        User user = Instancio.create(User.class);
        String token = "valid-token";

        when(securityService.parseToken(token)).thenReturn(Optional.of(user));

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/product/delete");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(request.getAttribute("user")).isEqualTo(user);
        verify(chain, times(1)).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    @DisplayName("Запрос без токена к защищенному пути возвращает 401")
    void requestWithoutTokenReturnsUnauthorized() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/product/delete");
        request.setCharacterEncoding("UTF-8");

        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setCharacterEncoding("UTF-8");

        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentAsString()).contains("Нет токена или токен неверный");
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("Доступ к публичному пути пропускает фильтр")
    void publicPathIsAllowed() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
    }
}