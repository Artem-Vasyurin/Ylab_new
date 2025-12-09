package vasyurin.work.utilites;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Утилитный компонент для получения соединения с PostgreSQL.
 * <p>
 * Использует параметры подключения из настроек Spring:
 * {@code db.url}, {@code db.username}, {@code db.password}.
 * <p>
 * В случае ошибки подключения выбрасывает {@link RuntimeException}.
 */
@Component
public class ConnectionTemplate implements ConnectionProvider {
    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    /**
     * Получает новое соединение с базой данных PostgreSQL.
     *
     * @return объект {@link Connection}
     * @throws RuntimeException если не удалось установить соединение или найти драйвер
     */
    @Override
    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }
}

