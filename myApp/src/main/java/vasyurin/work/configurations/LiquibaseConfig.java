package vasyurin.work.configurations;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import vasyurin.work.utilites.ConnectionTemplate;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Конфигурация и запуск Liquibase миграций для проекта.
 * <p>
 * Отвечает за:
 * <ul>
 *     <li>Создание схем в базе данных, если их нет</li>
 *     <li>Инициализацию Liquibase с заданным changeLog</li>
 *     <li>Применение миграций к базе данных</li>
 * </ul>
 * <p>
 * Использует {@link ConnectionTemplate} для получения JDBC соединения.
 * Свойства конфигурации берутся из environment:
 * <ul>
 *     <li>{@code liquibase.changeLog} — путь к Liquibase changelog файлу</li>
 *     <li>{@code liquibase.defaultSchema} — схема по умолчанию для приложения</li>
 *     <li>{@code liquibase.liquibaseSchemaName} — схема для внутренних таблиц Liquibase</li>
 * </ul>
 */

@Slf4j
@Component
@Profile("!test")
public class LiquibaseConfig {

    private static final String CREATE_SCHEMA_QUERY = "CREATE SCHEMA IF NOT EXISTS ";
    private final ConnectionTemplate connectionTemplate;
    @Value("${liquibase.changeLog}")
    private String changeLog;
    @Value("${liquibase.defaultSchema}")
    private String defaultSchema;
    @Value("${liquibase.liquibaseSchemaName}")
    private String liquibaseSchema;

    @Autowired
    public LiquibaseConfig(ConnectionTemplate connectionTemplate) {
        this.connectionTemplate = connectionTemplate;
    }

    /**
     * Запускает миграции базы данных с помощью Liquibase.
     * <p>
     * Создаёт схемы по умолчанию, инициализирует Liquibase с заданным {@code changeLog}
     * и применяет миграции.
     *
     * @throws IllegalStateException если не найдены обязательные свойства конфигурации
     * @throws RuntimeException      если миграции не удалось выполнить
     */
    public void runLiquibase() {
        log.info("Starting Liquibase ...");

        if (changeLog == null || defaultSchema == null || liquibaseSchema == null) {
            throw new IllegalStateException("Required Liquibase properties not found in environment!");
        }

        try (Connection conn = connectionTemplate.getConnection()) {

            try (Statement statement = conn.createStatement()) {
                statement.execute(CREATE_SCHEMA_QUERY + defaultSchema);
                statement.execute(CREATE_SCHEMA_QUERY + liquibaseSchema);
            }

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(conn));

            database.setDefaultSchemaName(defaultSchema);
            database.setLiquibaseSchemaName(liquibaseSchema);

            Liquibase liquibase = new Liquibase(
                    changeLog,
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update("");

        } catch (Exception e) {
            throw new RuntimeException("Liquibase migration failed", e);
        }
    }
}
