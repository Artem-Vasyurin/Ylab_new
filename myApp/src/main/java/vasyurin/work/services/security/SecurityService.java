package vasyurin.work.services.security;

import vasyurin.work.dto.User;

import java.util.Optional;

public interface SecurityService {
    Optional<String> login(User loginRequest);

    Optional<User> parseToken(String token);

}
