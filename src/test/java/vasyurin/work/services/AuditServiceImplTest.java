package vasyurin.work.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vasyurin.work.dto.User;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AuditServiceImplTest {

    private AuditServiceImpl auditService;

    @BeforeEach
    void setUp() {
        auditService = AuditServiceImpl.getInstance();
    }

    @Test
    void getInstance_returnsSameInstance() {
        AuditServiceImpl instance1 = AuditServiceImpl.getInstance();
        AuditServiceImpl instance2 = AuditServiceImpl.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void log_withUser_doesNotThrow() {
        User user = new User();
        user.setUsername("testUser");

        assertDoesNotThrow(() -> auditService.log(user, "Test action"));
    }

    @Test
    void log_withoutUser_doesNotThrow() {
        assertDoesNotThrow(() -> auditService.log((User) null, "Test action"));
    }

    @Test
    void log_actionOnly_doesNotThrow() {
        assertDoesNotThrow(() -> auditService.log("Test system action"));
    }
}
