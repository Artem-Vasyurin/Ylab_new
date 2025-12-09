package vasyurin.work.services.security;

import loggermetricksaspect.annotations.LoggingServices;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;
import vasyurin.work.dto.User;
import vasyurin.work.repository.UserRepository;
import vasyurin.work.services.interfases.JwtService;
import vasyurin.work.services.interfases.SecurityService;

import java.util.Optional;

/**
 * Сервис для аутентификации пользователей и работы с JWT-токенами.
 * <p>
 * Использует {@link UserRepository} для проверки логина и пароля,
 * а также {@link JwtService} для генерации и парсинга токенов.
 * <p>
 * Предоставляет методы для входа пользователя и извлечения данных из токена.
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public SecurityServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;

    }

    /**
     * Выполняет аутентификацию пользователя по username и паролю.
     * <p>
     * Если логин и пароль верны — генерирует JWT-токен через {@link JwtService}.
     *
     * @param loginRequest объект {@link User} с username и паролем для входа
     * @return {@link Optional} с токеном, если аутентификация успешна, иначе пустой
     */
    @Override
    @LoggingServices
    public Optional<String> login(User loginRequest) {
        if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return Optional.empty();
        }
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginRequest.getPassword())) {

            String token = jwtService.generateToken(userOpt.get());
            return Optional.of(token);
        }
        return Optional.empty();
    }

    /**
     * Разбирает JWT-токен и извлекает данные пользователя.
     * <p>
     * В случае недействительного или пустого токена возвращает пустой Optional.
     *
     * @param token JWT-токен для разбора
     * @return {@link Optional} с объектом {@link User}, если токен валиден, иначе пустой
     */
    @Override
    public Optional<User> parseToken(String token) {
        if (token == null) return Optional.empty();
        try {
            return Optional.ofNullable(jwtService.parseToken(token));
        } catch (JwtException e) {
            return Optional.empty();
        }
    }
}