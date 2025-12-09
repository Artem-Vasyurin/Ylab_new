package vasyurin.work.repository.sql;

/**
 * Реализация интерфейса {@link UserSqlProvider} для тестовых SQL-запросов пользователей.
 * <p>
 * Этот класс возвращает строки SQL-запросов, определенные в {@link UserSqlRequestTest}.
 * Используется для тестирования или работы с тестовой БД.
 * </p>
 */
public class UserSqlTestImpl implements UserSqlProvider {
    /**
     * Возвращает SQL-запрос для вставки нового пользователя.
     *
     * @return строка SQL для вставки пользователя ({@link UserSqlRequestTest#INSERT_USER})
     */
    public String getInsert() {
        return UserSqlRequestTest.INSERT_USER;
    }

    /**
     * Возвращает SQL-запрос для обновления информации о пользователе.
     *
     * @return строка SQL для обновления пользователя ({@link UserSqlRequestTest#UPDATE_USER})
     */
    public String getUpdate() {
        return UserSqlRequestTest.UPDATE_USER;
    }

    /**
     * Возвращает SQL-запрос для получения пользователя по его имени.
     *
     * @return строка SQL для выборки пользователя по имени ({@link UserSqlRequestTest#SELECT_BY_USERNAME_USER})
     */
    public String getSelectByUsername() {
        return UserSqlRequestTest.SELECT_BY_USERNAME_USER;
    }

    /**
     * Возвращает SQL-запрос для получения всех пользователей.
     *
     * @return строка SQL для выборки всех пользователей ({@link UserSqlRequestTest#SELECT_ALL_USER})
     */
    public String getSelectAll() {
        return UserSqlRequestTest.SELECT_ALL_USER;
    }
}