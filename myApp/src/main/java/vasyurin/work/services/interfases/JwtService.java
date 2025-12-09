package vasyurin.work.services.interfases;

import vasyurin.work.dto.User;

/**
 * Интерфейс для работы с JWT-токенами.
 * Определяет методы генерации и парсинга JWT для пользователей.
 */
public interface JwtService {
    /**
     * Генерирует JWT-токен для указанного пользователя.
     *
     * @param user пользователь, для которого создаётся токен
     * @return JWT-токен
     */
    String generateToken(User user);

    /**
     * Извлекает пользователя из JWT-токена.
     *
     * @param token JWT-токен
     * @return объект пользователя, если токен валиден
     */
    User parseToken(String token);
}