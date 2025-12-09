package vasyurin.work.utilites;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Интерфейс для предоставления соединений с базой данных.
 * Используется только в тестах.
 */
public interface ConnectionProvider {
    /**
     * Возвращает соединение с базой данных.
     *
     * @return объект соединения {@link Connection}
     * @throws SQLException если не удалось получить соединение
     */
    Connection getConnection() throws SQLException;
}