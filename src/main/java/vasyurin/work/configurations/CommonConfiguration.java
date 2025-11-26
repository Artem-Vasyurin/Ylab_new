package vasyurin.work.configurations;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Getter;
import lombok.experimental.StandardException;
import lombok.extern.slf4j.Slf4j;
import vasyurin.work.utilites.ConnectionTemplate;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Конфигурационный класс, отвечающий за инициализацию схем
 * и выполнение миграций базы данных с помощью Liquibase.
 */

@Slf4j
public class CommonConfiguration {

    @Getter
    private static final CommonConfiguration instance = new CommonConfiguration();

    private static final String CHANGELOG = ConfigurationReader.readConfiguration().liquibase().changeLog();
    private static final String DEFAULT_SCHEMA = ConfigurationReader.readConfiguration().liquibase().defaultSchema();
    private static final String LIQUIBASE_SCHEMA = ConfigurationReader.readConfiguration().liquibase().liquibaseSchemaName();
    private static final String CREATE_SCHEMA_QUERY = "CREATE SCHEMA IF NOT EXISTS ";


    private CommonConfiguration() {
    }

    public void runLiquibase() {
        log.info("Starting Liquibase ...");

        try (Connection conn = ConnectionTemplate.getConnection()) {

            try (Statement statement = conn.createStatement()) {
                statement.execute(CREATE_SCHEMA_QUERY + DEFAULT_SCHEMA);
                statement.execute(CREATE_SCHEMA_QUERY + LIQUIBASE_SCHEMA);
            }

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(conn));

            database.setDefaultSchemaName(DEFAULT_SCHEMA);
            database.setLiquibaseSchemaName(LIQUIBASE_SCHEMA);

            Liquibase liquibase = new Liquibase(
                    CHANGELOG,
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update("");

        } catch (Exception e) {
            throw new CommonConfiguration.CommonConfigurationException(e.getMessage(), e);
        }
    }

    @StandardException
    public static class CommonConfigurationException extends RuntimeException {
    }
}
