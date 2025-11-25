package vasyurin.work.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.User;
import vasyurin.work.services.security.SecurityServiceImpl;

import java.io.*;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Testcontainers
public class LoginServletTest {

    private LoginServlet servlet;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private SecurityServiceImpl securityServiceMock;
    private ObjectMapper mapper = new ObjectMapper();
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        securityServiceMock = mock(SecurityServiceImpl.class);
        servlet = new LoginServlet(securityServiceMock);

        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);

        responseWriter = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void testDoPost_SuccessfulLogin() throws Exception {
        User loginRequest = new User();
        loginRequest.setUsername("test");
        loginRequest.setPassword("123");

        String json = mapper.writeValueAsString(loginRequest);
        when(req.getInputStream()).thenReturn(
                new DelegatingServletInputStream(new ByteArrayInputStream(json.getBytes()))
        );

        User loggedUser = new User();
        loggedUser.setToken("TOKEN123");
        when(securityServiceMock.login(any())).thenReturn(Optional.of(loggedUser));

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        String responseJson = responseWriter.toString();
        Map<?, ?> map = mapper.readValue(responseJson, Map.class);
        assertEquals("TOKEN123", map.get("token"));
    }

    @Test
    void testDoPost_InvalidCredentials() throws Exception {
        User loginRequest = new User();
        loginRequest.setUsername("wrong");
        loginRequest.setPassword("wrong");

        String json = mapper.writeValueAsString(loginRequest);
        when(req.getInputStream()).thenReturn(
                new DelegatingServletInputStream(new ByteArrayInputStream(json.getBytes()))
        );

        when(securityServiceMock.login(any())).thenReturn(Optional.empty());

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String response = responseWriter.toString();
        assertEquals("{\"error\":\"Неверный логин или пароль\"}", response);
    }

    @Test
    void testDoPost_InvalidJson() throws Exception {
        String badJson = "{bad json}";
        when(req.getInputStream()).thenReturn(
                new DelegatingServletInputStream(new ByteArrayInputStream(badJson.getBytes()))
        );

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String response = responseWriter.toString();
        assertEquals("{\"error\":\"Некорректный JSON\"}", response);
    }

    public static class DelegatingServletInputStream extends jakarta.servlet.ServletInputStream {
        private final InputStream sourceStream;

        public DelegatingServletInputStream(InputStream sourceStream) {
            this.sourceStream = sourceStream;
        }

        @Override
        public int read() throws IOException {
            return sourceStream.read();
        }

        @Override
        public boolean isFinished() {
            try {
                return sourceStream.available() == 0;
            } catch (IOException e) {
                return true;
            }
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(jakarta.servlet.ReadListener readListener) {
        }
    }
}
