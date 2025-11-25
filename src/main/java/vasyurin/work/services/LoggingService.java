package vasyurin.work.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LoggingService {

    private static final String FILE_PATH = "/app/logs/execution.log";

    public static void logExecution(String message) {
        String log = String.format("[%s] %s", LocalDateTime.now(), message);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(log);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка записи execution.log: " + e.getMessage());
        }
    }
}
