package vasyurin.work.controllers;

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

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        Optional<User> userOpt = securityService.login(loginRequest);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return ResponseEntity.ok(Map.of("token", user.getToken()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неверный логин или пароль"));
        }
    }
}
