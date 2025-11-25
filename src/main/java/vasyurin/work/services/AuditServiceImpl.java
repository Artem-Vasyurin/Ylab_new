package vasyurin.work.services;

import lombok.Getter;
import vasyurin.work.dto.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditServiceImpl implements AuditService {

    @Getter
    private static final AuditServiceImpl instance = new AuditServiceImpl();
    private static final String FILE_PATH = "/app/logs/audit.log";

    private AuditServiceImpl() {}

    @Override
    public void log(User user, String action) {
        String log = String.format("[%s] Пользователь: %s  Действие: %s",
                LocalDateTime.now(), user != null ? user.getUsername() : "SYSTEM", action);
        writeLog(log);
    }

    @Override
    public void log(String action) {
        String log = String.format("[%s] Действие: %s", LocalDateTime.now(), action);
        writeLog(log);
    }

    private void writeLog(String log) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(log);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка при записи аудита: " + e.getMessage());
        }
    }
}
