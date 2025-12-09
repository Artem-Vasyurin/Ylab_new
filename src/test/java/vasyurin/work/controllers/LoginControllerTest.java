package vasyurin.work.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.User;
import vasyurin.work.services.security.SecurityService;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Testcontainers
class LoginControllerTest {

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createTestUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setToken("mocked-token-abc");
        return user;
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        User loginRequest = createTestUser();
        Optional<User> returnedUser = Optional.of(loginRequest);

        when(securityService.login(loginRequest)).thenReturn(returnedUser);

        ResponseEntity<?> response = loginController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mocked-token-abc", ((Map<?, ?>) response.getBody()).get("token"));

        verify(securityService, times(1)).login(loginRequest);
    }

    @Test
    void login_shouldReturnUnauthorized_whenCredentialsAreInvalid() {
        User loginRequest = createTestUser();

        when(securityService.login(loginRequest)).thenReturn(Optional.empty());

        ResponseEntity<?> response = loginController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Неверный логин или пароль", ((Map<?, ?>) response.getBody()).get("error"));

        verify(securityService, times(1)).login(loginRequest);
    }
}
