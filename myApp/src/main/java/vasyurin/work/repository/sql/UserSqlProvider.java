package vasyurin.work.repository.sql;

/**
 * Интерфейс для предоставления SQL-запросов для работы с пользователями.
 * Каждый метод возвращает SQL-запрос соответствующего типа.
 */
public interface UserSqlProvider {
    /**
     * Возвращает SQL-запрос для вставки нового пользователя в базу данных.
     *
     * @return SQL-запрос INSERT
     */
    String getInsert();

    /**
     * Возвращает SQL-запрос для обновления существующего пользователя.
     *
     * @return SQL-запрос UPDATE
     */
    String getUpdate();

    /**
     * Возвращает SQL-запрос для выборки пользователя по имени пользователя.
     *
     * @return SQL-запрос SELECT с условием по username
     */
    String getSelectByUsername();

    /**
     * Возвращает SQL-запрос для выборки всех пользователей.
     *
     * @return SQL-запрос SELECT всех пользователей
     */
    String getSelectAll();
}
