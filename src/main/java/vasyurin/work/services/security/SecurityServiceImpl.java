package vasyurin.work.services.security;

import lombok.Getter;
import vasyurin.work.dto.User;

public class SecurityServiceImpl implements SecurityService {

    @Getter
    private static final SecurityServiceImpl instance = new SecurityServiceImpl();

    private SecurityServiceImpl() {}

    @Override
    public boolean isAdmin(User user) {
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            System.out.println("Только администратор может это сделать.");
            return false;
        }
        return true;
    }

    @Override
    public boolean isAuthenticated(User user) {
        return user != null;
    }
}
