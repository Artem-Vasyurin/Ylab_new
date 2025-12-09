package vasyurin.work.services.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import vasyurin.work.dto.User;
import vasyurin.work.enams.UserRole;
import vasyurin.work.services.interfases.JwtService;

import java.security.Key;
import java.util.Date;

/**
 * Сервис для генерации и проверки JWT-токенов.
 * <p>
 * Использует алгоритм HS256 для подписи токена.
 * Срок жизни токена — 24 часа.
 * <p>
 * Предоставляет методы для создания токена пользователя и извлечения данных пользователя из токена.
 */
@Service
public class JwtServiceImpl implements JwtService {

    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000;
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Генерирует JWT-токен для пользователя.
     * <p>
     * Токен содержит username и роль пользователя, дату создания и срок действия.
     *
     * @param user пользователь для которого создаётся токен
     * @return JWT-токен в виде строки
     */
    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    /**
     * Разбирает JWT-токен и извлекает данные пользователя.
     * <p>
     * Извлекаются username и роль пользователя. Пароль и токен в объекте User остаются пустыми.
     *
     * @param token JWT-токен для разбора
     * @return объект {@link User} с username и ролью
     * @throws JwtException если токен недействителен или повреждён
     */
    @Override
    public User parseToken(String token) throws JwtException {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        Claims claims = claimsJws.getBody();
        String username = claims.getSubject();
        String roleStr = claims.get("role", String.class);
        UserRole role = UserRole.valueOf(roleStr);

        return new User(username, "", role, "");
    }
}