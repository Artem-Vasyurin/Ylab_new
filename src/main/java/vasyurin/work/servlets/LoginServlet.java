package vasyurin.work.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;
import vasyurin.work.dto.User;
import vasyurin.work.services.security.SecurityServiceImpl;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final SecurityServiceImpl securityService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginServlet() {
        this.securityService = SecurityServiceImpl.getInstance();
    }

    public LoginServlet(SecurityServiceImpl securityServiceMock) {
        this.securityService = securityServiceMock;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        User loginRequest;
        try {
            loginRequest = objectMapper.readValue(req.getInputStream(), User.class);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Некорректный JSON\"}");
            return;
        }

        Optional<User> userOpt = securityService.login(loginRequest);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("token", user.getToken())));
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Неверный логин или пароль\"}");
        }
    }
}

