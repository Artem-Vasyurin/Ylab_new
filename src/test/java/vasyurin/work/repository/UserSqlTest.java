package vasyurin.work.repository;

public class UserSqlTest {

    public static final String INSERT_USER = """
        INSERT INTO test_schema.users (username, password, role, token) VALUES (?, ?, ?, ?)
    """;

    public static final String UPDATE_USER = """
        UPDATE test_schema.users SET password = ?, role = ?, token = ? WHERE username = ?
    """;

    public static final String SELECT_BY_USERNAME_USER = """
        SELECT username, password, role, token FROM test_schema.users WHERE username = ?
    """;

    public static final String SELECT_ALL_USER = """
        SELECT username, password, role, token FROM test_schema.users
    """;

    public static final String CREATE_TABLE_USERS = """
        CREATE TABLE IF NOT EXISTS test_schema.users (
            username VARCHAR(255) PRIMARY KEY,
            password VARCHAR(255) NOT NULL,
            role VARCHAR(50),
            token VARCHAR(255)
        );
    """;

    public static final String CREATE_SCHEMA_USERS = """
        CREATE SCHEMA IF NOT EXISTS test_schema
    """;

    public static final String DELETE_TABELE_USERS = """
        DELETE FROM test_schema.users
    """;
}
