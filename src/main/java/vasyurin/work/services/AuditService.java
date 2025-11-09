package vasyurin.work.services;

import lombok.Getter;
import vasyurin.work.dto.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditService {

    @Getter
    private static final AuditService instance = new AuditService();
    private static final String FILE_PATH = "audit.log";

    private AuditService() {}

    public void log(User user, String action) {
        String log = String.format("[%s] Пользователь: %s  Действие: %s",
                LocalDateTime.now(), user != null ? user.getUsername() : "SYSTEM", action);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(log);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка при записи аудита: " + e.getMessage());
        }
    }
}
