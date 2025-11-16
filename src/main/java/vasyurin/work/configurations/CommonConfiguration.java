package vasyurin.work.configurations;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Getter;
import vasyurin.work.utilites.ConnectionTemplate;

import java.sql.Connection;
import java.sql.Statement;

public class CommonConfiguration {

    @Getter
    private static final CommonConfiguration instance = new CommonConfiguration();

    private static final String CHANGELOG = ConfigurationReader.readConfiguration().liquibase().changeLog();
    private static final String DEFAULT_SCHEMA = ConfigurationReader.readConfiguration().liquibase().defaultSchema();
    private static final String LIQUIBASE_SCHEMA = ConfigurationReader.readConfiguration().liquibase().liquibaseSchemaName();


    private CommonConfiguration() { }

    public void runLiquibase() {

        try (Connection conn = ConnectionTemplate.getConnection()) {

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE SCHEMA IF NOT EXISTS " + DEFAULT_SCHEMA);
                stmt.execute("CREATE SCHEMA IF NOT EXISTS " + LIQUIBASE_SCHEMA);
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
            throw new RuntimeException("Liquibase migration failed", e);
        }
    }
}
