package vasyurin.work.dto;

public record ConfigDto(Db db, Liquibase liquibase) {

    public record Db(
            String url,
            String username,
            String password,
            String schema,
            String serviceSchema,
            String usernameTest,
            String passwordTest,
            String schemaTest,
            String serviceSchemaTest

    ) {}

    public record Liquibase(
            String changeLog,
            String defaultSchema,
            String liquibaseSchemaName,
            String changeLogTest,
            String defaultSchemaTest,
            String liquibaseSchemaNameTest
    ) {}
}
