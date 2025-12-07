package vasyurin.work.services.security;

import vasyurin.work.dto.User;

import java.util.Optional;

public interface SecurityService {
    Optional<User> getByToken(String token);

    Optional<User> login(User loginRequest);
}
