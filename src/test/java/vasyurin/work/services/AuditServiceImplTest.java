package vasyurin.work.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@Testcontainers
class AuditServiceImplTest {

    private AuditServiceImpl auditSpy;
    private List<String> capturedLogs;

    @BeforeEach
    void setUp() {
        capturedLogs = new ArrayList<>();
        auditSpy = spy(AuditServiceImpl.getInstance());

        doAnswer(invocation -> {
            String log = invocation.getArgument(0);
            capturedLogs.add(log);
            return null;
        }).when(auditSpy).log(anyString());

        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            String action = invocation.getArgument(1);
            String log = String.format("[%s] Пользователь: %s  Действие: %s",
                    java.time.LocalDateTime.now(),
                    user != null ? user.getUsername() : "SYSTEM",
                    action);
            capturedLogs.add(log);
            return null;
        }).when(auditSpy).log(any(User.class), anyString());
    }

    @Test
    void testLogWithoutUser() {
        auditSpy.log("Система перезапущена");

        assertEquals(1, capturedLogs.size());
        assertTrue(capturedLogs.getFirst().contains("Система перезапущена"));
    }

    @Test
    void testLogWithUser() {
        User user = new User();
        user.setUsername("admin");

        auditSpy.log(user, "Создан товар");

        assertEquals(1, capturedLogs.size());
        String log = capturedLogs.getFirst();
        assertTrue(log.contains("admin"));
        assertTrue(log.contains("Создан товар"));
    }
}
