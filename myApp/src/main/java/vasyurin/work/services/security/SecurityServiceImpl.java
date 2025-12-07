package vasyurin.work.services.security;

import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;
import annotations.LoggingServices;
import vasyurin.work.dto.User;
import vasyurin.work.repository.UserRepository;

import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public SecurityServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;

    }

    @Override
    @LoggingServices
    public Optional<String> login(User loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginRequest.getPassword())) {

            String token = jwtService.generateToken(userOpt.get());
            return Optional.of(token);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> parseToken(String token) {
        try {
            return Optional.of(jwtService.parseToken(token));
        } catch (JwtException e) {
            return Optional.empty();
        }
    }
}