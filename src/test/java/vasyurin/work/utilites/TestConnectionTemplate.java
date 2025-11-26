package vasyurin.work.utilites;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnectionTemplate implements ConnectionProvider {

    private final String url;
    private final String user;
    private final String pass;

    public TestConnectionTemplate(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}