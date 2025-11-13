package vasyurin.work.services.security;

import vasyurin.work.dto.User;

public interface SecurityService {
    boolean isAdmin(User user);
    boolean isAuthenticated(User user);
}
