package vasyurin.work.utilites;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import vasyurin.work.configurations.LiquibaseConfig;

@Component
public class LiquibaseRunner {

    private final LiquibaseConfig liquibaseConfig;

    public LiquibaseRunner(LiquibaseConfig liquibaseConfig) {
        this.liquibaseConfig = liquibaseConfig;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void run() {
        liquibaseConfig.runLiquibase();
    }
}

