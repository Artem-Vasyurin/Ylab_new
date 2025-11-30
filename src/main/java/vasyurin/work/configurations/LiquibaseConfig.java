package vasyurin.work.configurations;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import vasyurin.work.utilites.ConnectionTemplate;

import java.sql.Connection;
import java.sql.Statement;

@Slf4j
@Component
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
    public LiquibaseConfig(ConnectionTemplate connectionTemplate, Environment env) {
        this.connectionTemplate = connectionTemplate;
    }

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
