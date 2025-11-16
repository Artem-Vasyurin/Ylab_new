package vasyurin.work.services.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vasyurin.work.dto.User;

import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceImplTest {

    private SecurityServiceImpl securityService;

    @BeforeEach
    void setUp() {
        securityService = SecurityServiceImpl.getInstance();
    }

    @Test
    void getInstance_returnsSameInstance() {
        SecurityServiceImpl instance1 = SecurityServiceImpl.getInstance();
        SecurityServiceImpl instance2 = SecurityServiceImpl.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void isAdmin_returnsTrueForAdmin() {
        User admin = new User();
        admin.setRole("ADMIN");
        assertTrue(securityService.isAdmin(admin));
    }

    @Test
    void isAdmin_returnsFalseForNonAdmin() {
        User user = new User();
        user.setRole("USER");
        assertFalse(securityService.isAdmin(user));
    }

    @Test
    void isAdmin_returnsFalseForNull() {
        assertFalse(securityService.isAdmin(null));
    }

    @Test
    void isAuthenticated_returnsTrueForUser() {
        User user = new User();
        assertTrue(securityService.isAuthenticated(user));
    }

    @Test
    void isAuthenticated_returnsFalseForNull() {
        assertFalse(securityService.isAuthenticated(null));
    }
}
