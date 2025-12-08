package vasyurin.work.services.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import vasyurin.work.dto.User;
import vasyurin.work.enams.UserRole;
import vasyurin.work.services.interfases.JwtService;


import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000;
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

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
