//package vasyurin.work.controllers;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import vasyurin.work.dto.User;
//import vasyurin.work.dto.UserContext;
//import vasyurin.work.services.security.SecurityServiceImpl;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.mockito.Mockito.*;
//
//@Testcontainers
//public class AuthFilterTest {
//
//    private AuthFilter filter;
//    private SecurityServiceImpl securityServiceMock;
//    private HttpServletRequest req;
//    private HttpServletResponse resp;
//    private FilterChain chain;
//    private StringWriter responseWriter;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        securityServiceMock = mock(SecurityServiceImpl.class);
//        filter = new AuthFilter(securityServiceMock);
//
//        req = mock(HttpServletRequest.class);
//        resp = mock(HttpServletResponse.class);
//        chain = mock(FilterChain.class);
//
//        responseWriter = new StringWriter();
//        when(resp.getWriter()).thenReturn(new PrintWriter(responseWriter));
//    }
//
//    @Test
//    void testNoToken() throws Exception {
//        when(req.getHeader("X-Auth-Token")).thenReturn(null);
//
//        filter.doFilter(req, resp, chain);
//
//        verify(resp).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        assertEquals("{\"error\":\"Отсутствует токен\"}", responseWriter.toString());
//        verify(chain, never()).doFilter(any(), any());
//    }
//
//    @Test
//    void testInvalidToken() throws Exception {
//        when(req.getHeader("X-Auth-Token")).thenReturn("badtoken");
//        when(securityServiceMock.getByToken("badtoken")).thenReturn(Optional.empty());
//
//        filter.doFilter(req, resp, chain);
//
//        verify(resp).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        assertEquals("{\"error\":\"Неверный токен\"}", responseWriter.toString());
//        verify(chain, never()).doFilter(any(), any());
//    }
//
//    @Test
//    void testUserNotAdmin() throws Exception {
//        User user = new User();
//        user.setRole("USER");
//
//        when(req.getHeader("X-Auth-Token")).thenReturn("token");
//        when(securityServiceMock.getByToken("token")).thenReturn(Optional.of(user));
//
//        filter.doFilter(req, resp, chain);
//
//        verify(resp).setStatus(HttpServletResponse.SC_FORBIDDEN);
//        assertEquals("{\"error\":\"Нет прав для доступа\"}", responseWriter.toString());
//        verify(chain, never()).doFilter(any(), any());
//    }
//
//    @Test
//    void testUserIsAdmin() throws Exception {
//        User admin = new User();
//        admin.setRole("ADMIN");
//
//        when(req.getHeader("X-Auth-Token")).thenReturn("admintoken");
//        when(securityServiceMock.getByToken("admintoken")).thenReturn(Optional.of(admin));
//
//        filter.doFilter(req, resp, chain);
//
//        verify(chain, times(1)).doFilter(req, resp);
//
//        assertNull(UserContext.get());
//    }
//}
