package vasyurin.work.controllers;

import lombok.Getter;
import vasyurin.work.dto.User;
import vasyurin.work.repository.UserRepositoryImpl;
import java.util.Optional;

public class AuthController {

    @Getter
    private static final AuthController instance = new AuthController();

    private final UserRepositoryImpl userRepository;

    private AuthController() {
        this.userRepository = UserRepositoryImpl.getInstance();
    }

    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
}
