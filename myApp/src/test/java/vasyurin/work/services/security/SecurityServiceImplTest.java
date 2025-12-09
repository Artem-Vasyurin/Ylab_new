package vasyurin.work.services.security;

import io.jsonwebtoken.JwtException;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vasyurin.work.dto.User;
import vasyurin.work.repository.UserRepository;
import vasyurin.work.services.interfases.JwtService;
import vasyurin.work.services.interfases.SecurityService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityServiceImplTest {

    private SecurityService securityService;
    private UserRepository userRepositoryMock;
    private JwtService jwtServiceMock;

    @BeforeEach
    void setUp() {
        userRepositoryMock = mock(UserRepository.class);
        jwtServiceMock = mock(JwtService.class);
        securityService = new SecurityServiceImpl(userRepositoryMock, jwtServiceMock);
    }

    @Test
    @DisplayName("Успешный логин возвращает токен")
    void testLogin_Success() {
        User loginRequest = Instancio.create(User.class);
        loginRequest.setUsername("test");
        loginRequest.setPassword("123");

        User userFromDb = Instancio.create(User.class);
        userFromDb.setUsername("test");
        userFromDb.setPassword("123");

        when(userRepositoryMock.findByUsername("test")).thenReturn(Optional.of(userFromDb));
        when(jwtServiceMock.generateToken(userFromDb)).thenReturn("token123");

        Optional<String> result = securityService.login(loginRequest);

        assertThat(result).isPresent().contains("token123");
        verify(userRepositoryMock).findByUsername("test");
        verify(jwtServiceMock).generateToken(userFromDb);
    }

    @Test
    @DisplayName("Логин с неверным паролем возвращает пустой Optional")
    void testLogin_WrongPassword() {
        User loginRequest = Instancio.create(User.class);
        loginRequest.setUsername("test");
        loginRequest.setPassword("wrong");

        User userFromDb = Instancio.create(User.class);
        userFromDb.setUsername("test");
        userFromDb.setPassword("123");

        when(userRepositoryMock.findByUsername("test")).thenReturn(Optional.of(userFromDb));

        Optional<String> result = securityService.login(loginRequest);

        assertThat(result).isEmpty();
        verify(userRepositoryMock).findByUsername("test");
        verifyNoInteractions(jwtServiceMock);
    }

    @Test
    @DisplayName("Логин с несуществующим пользователем возвращает пустой Optional")
    void testLogin_UserNotFound() {
        User loginRequest = Instancio.create(User.class);
        loginRequest.setUsername("unknown");

        when(userRepositoryMock.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<String> result = securityService.login(loginRequest);

        assertThat(result).isEmpty();
        verify(userRepositoryMock).findByUsername("unknown");
        verifyNoInteractions(jwtServiceMock);
    }

    @Test
    @DisplayName("Логин с null запросом возвращает пустой Optional")
    void testLogin_NullRequest() {
        assertThat(securityService.login(null)).isEmpty();
    }

    @Test
    @DisplayName("Разбор валидного токена возвращает пользователя")
    void testParseToken_Success() {
        User user = Instancio.create(User.class);
        when(jwtServiceMock.parseToken("token1")).thenReturn(user);

        Optional<User> result = securityService.parseToken("token1");

        assertThat(result).isPresent().contains(user);
        verify(jwtServiceMock).parseToken("token1");
    }

    @Test
    @DisplayName("Разбор некорректного токена возвращает пустой Optional")
    void testParseToken_InvalidToken() {
        when(jwtServiceMock.parseToken("badToken")).thenThrow(new JwtException("Invalid token"));


        Optional<User> result = securityService.parseToken("badToken");

        assertThat(result).isEmpty();
        verify(jwtServiceMock).parseToken("badToken");
    }

    @Test
    @DisplayName("Разбор null токена возвращает пустой Optional")
    void testParseToken_NullToken() {
        Optional<User> result = securityService.parseToken(null);
        assertThat(result).isEmpty();
        verifyNoInteractions(jwtServiceMock);
    }
}