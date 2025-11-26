package vasyurin.work.services.security;

import lombok.Getter;
import vasyurin.work.annotations.AuditAction;
import vasyurin.work.dto.User;
import vasyurin.work.repository.UserRepository;
import vasyurin.work.repository.UserRepositoryImplPostgres;

import java.util.List;
import java.util.Optional;

public class SecurityServiceImpl implements SecurityService {

    @Getter
    private static final SecurityServiceImpl instance = new SecurityServiceImpl();
    private final UserRepository userRepository = UserRepositoryImplPostgres.getInstance();

    private SecurityServiceImpl() {
    }

    @AuditAction
    @Override
    public Optional<User> login(User loginRequest) {
        if (loginRequest == null
            || loginRequest.getUsername() == null
            || loginRequest.getPassword() == null) {
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getByToken(String token) {
        if (token == null) return Optional.empty();
        List<User> users = userRepository.getAll();
        return users.stream().filter(u -> token.equals(u.getToken())).findFirst();
    }
}
