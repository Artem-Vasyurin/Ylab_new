package vasyurin.work.repository;

public class UserSql {

    public static final String INSERT_USER = """
            INSERT INTO app_schema.users (username, password, role, token) VALUES (?, ?, ?, ?)
            """;

    public static final String UPDATE_USER = """
            UPDATE app_schema.users SET password=?, role=?, token=? WHERE username=?
            """;

    public static final String SELECT_BY_USERNAME_USER = """
            SELECT username, password, role, token FROM app_schema.users WHERE username=?
            """;

    public static final String SELECT_ALL_USER = """
            SELECT username, password, role, token FROM app_schema.users
            """;

}
