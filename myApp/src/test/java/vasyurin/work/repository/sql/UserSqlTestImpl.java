package vasyurin.work.repository.sql;

import org.springframework.stereotype.Service;

@Service
public class UserSqlTestImpl implements UserSqlProvider {
    public String getInsert() {
        return UserSqlRequestTest.INSERT_USER;
    }

    public String getUpdate() {
        return UserSqlRequestTest.UPDATE_USER;
    }

    public String getSelectByUsername() {
        return UserSqlRequestTest.SELECT_BY_USERNAME_USER;
    }

    public String getSelectAll() {
        return UserSqlRequestTest.SELECT_ALL_USER;
    }
}
