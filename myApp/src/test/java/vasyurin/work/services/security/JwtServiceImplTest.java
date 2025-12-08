package vasyurin.work.services.security;

import io.jsonwebtoken.JwtException;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vasyurin.work.dto.User;
import vasyurin.work.enams.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
    }

    @Test
    @DisplayName("Генерация и парсинг валидного токена")
    void testGenerateAndParseToken_Success() {
        User user = Instancio.of(User.class)
                .generate(field(User::getUsername), gen -> gen.text().pattern("testUser"))
                .generate(field(User::getPassword), gen -> gen.text().pattern("password"))
                .generate(field(User::getRole), gen -> gen.oneOf(UserRole.USER, UserRole.ADMIN))
                .create();

        String token = jwtService.generateToken(user);
        assertThat(token).isNotNull();

        User parsedUser = jwtService.parseToken(token);
        assertThat(parsedUser).isNotNull();
        assertThat(parsedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(parsedUser.getRole()).isEqualTo(user.getRole());
    }

    @Test
    @DisplayName("Парсинг некорректного токена выбрасывает JwtException")
    void testParseToken_InvalidToken() {
        String invalidToken = "invalid.token.value";

        assertThrows(JwtException.class, () -> jwtService.parseToken(invalidToken));
    }

    @Test
    @DisplayName("parseToken пустой токен")
    void testParseToken_EmptyToken() {
        assertThrows(IllegalArgumentException.class, () -> jwtService.parseToken(""));
    }
}
