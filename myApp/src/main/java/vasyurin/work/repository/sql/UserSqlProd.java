package vasyurin.work.repository.sql;

import org.springframework.stereotype.Service;

/**
 * Реализация {@link UserSqlProvider}, предоставляющая SQL-запросы
 * для работы с пользователями в продуктивной среде.
 */
@Service
public class UserSqlProd implements UserSqlProvider {

    /**
     * Возвращает SQL-запрос для вставки нового пользователя.
     */
    @Override
    public String getInsert() {
        return UserSqlRequest.INSERT_USER;
    }

    /**
     * Возвращает SQL-запрос для обновления существующего пользователя.
     */
    @Override
    public String getUpdate() {
        return UserSqlRequest.UPDATE_USER;
    }

    /**
     * Возвращает SQL-запрос для выборки пользователя по имени пользователя.
     */
    @Override
    public String getSelectByUsername() {
        return UserSqlRequest.SELECT_BY_USERNAME_USER;
    }

    /**
     * Возвращает SQL-запрос для выборки всех пользователей.
     */
    @Override
    public String getSelectAll() {
        return UserSqlRequest.SELECT_ALL_USER;
    }
}