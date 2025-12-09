package vasyurin.work.services.interfases;

import vasyurin.work.dto.User;

import java.util.Optional;

/**
 * Интерфейс для операций аутентификации и работы с токенами пользователей.
 */
public interface SecurityService {
    /**
     * Проводит аутентификацию пользователя.
     *
     * @param loginRequest данные для входа пользователя
     * @return Optional с токеном, если аутентификация успешна; пустой Optional иначе
     */
    Optional<String> login(User loginRequest);

    /**
     * Извлекает пользователя из токена.
     *
     * @param token токен пользователя
     * @return Optional с пользователем, если токен валиден; пустой Optional иначе
     */
    Optional<User> parseToken(String token);

}