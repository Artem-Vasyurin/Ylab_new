package vasyurin.work.services.interfases;

import vasyurin.work.dto.User;

public interface JwtService {
    String generateToken(User user);

    User parseToken(String token);
}
