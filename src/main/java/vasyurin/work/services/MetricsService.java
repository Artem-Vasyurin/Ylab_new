package vasyurin.work.services;

import lombok.Getter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class MetricsService {

    @Getter
    private static final MetricsService instance = new MetricsService();
    private static final String FILE_PATH = "metrics.log";

    private MetricsService() {}

    public void measureExecutionTime(String operationName, Runnable action) {
        Instant start = Instant.now();
        try {
            action.run();
        } finally {
            Instant end = Instant.now();
            long millis = Duration.between(start, end).toMillis();
            logMetric("Операция: " + operationName + " | Время выполнения: " + millis + " ms");
        }
    }

    public void logProductCount(int count) {
        logMetric("Всего товаров в системе: " + count);
    }

    private void logMetric(String message) {
        String logEntry = String.format("[%s] %s", LocalDateTime.now(), message);
        System.out.println("[METRICS] " + logEntry);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка при записи метрики: " + e.getMessage());
        }
    }
}
