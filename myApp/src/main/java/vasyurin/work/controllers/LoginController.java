package vasyurin.work.controllers;

import annotations.Auditing;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vasyurin.work.dto.User;
import vasyurin.work.services.security.SecurityService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final SecurityService securityService;

    @Auditing
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        Optional<String> tokenOpt = securityService.login(loginRequest);

        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok(Map.of("token", tokenOpt.get()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неверный логин или пароль"));
        }
    }
}
