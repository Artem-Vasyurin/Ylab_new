package vasyurin.work.controllers;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import vasyurin.work.dto.User;
import vasyurin.work.enams.UserRole;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class AuthorizationFilterTest {

    private final AuthorizationFilter filter = new AuthorizationFilter();

    @Test
    @DisplayName("User с ролью USER может выполнять POST /product/get")
    void userCanAccessGetProducts() throws ServletException, IOException {
        User user = Instancio.create(User.class);

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/product/get");
        request.setAttribute("user", user);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(200); // статус не меняется, так как chain.doFilter вызывается
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("User с ролью USER не может выполнять DELETE /product/delete")
    void userCannotDeleteProduct() throws ServletException, IOException {
        User user = Instancio.create(User.class);
        user.setRole(UserRole.USER);

        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/product/delete");
        request.setAttribute("user", user);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        response.setCharacterEncoding("UTF-8");

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
        assertThat(response.getContentAsString()).contains("Нет прав для данного действия");
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("Admin может выполнять DELETE /product/delete")
    void adminCanDeleteProduct() throws ServletException, IOException {
        User user = Instancio.create(User.class);
        user.setRole(UserRole.ADMIN);

        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/product/delete");
        request.setAttribute("user", user);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(200);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Если user не установлен, фильтр пропускает запрос")
    void nullUserPassesThrough() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/product/get");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(200);
        verify(chain, times(1)).doFilter(request, response);
    }
}