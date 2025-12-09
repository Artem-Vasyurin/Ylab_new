package vasyurin.work.utilites;

import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import vasyurin.work.configurations.LiquibaseConfig;

/**
 * Компонент для автоматического запуска Liquibase при старте приложения.
 * <p>
 * Срабатывает на событие {@link org.springframework.context.event.ContextRefreshedEvent}.
 * Работает только в профилях, отличных от "test".
 */
@Component
@Profile("!test")
public class LiquibaseRunner {

    private final LiquibaseConfig liquibaseConfig;

    public LiquibaseRunner(LiquibaseConfig liquibaseConfig) {
        this.liquibaseConfig = liquibaseConfig;
    }

    /**
     * Запускает миграции базы данных через {@link vasyurin.work.configurations.LiquibaseConfig}.
     * <p>
     * Вызывается автоматически при старте Spring-контекста.
     */
    @EventListener(ContextRefreshedEvent.class)
    public void run() {
        liquibaseConfig.runLiquibase();
    }
}