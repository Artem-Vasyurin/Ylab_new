package vasyurin.work.utilites;

import vasyurin.work.configurations.ConfigurationReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTemplate {

    private static final String URL = ConfigurationReader.readConfiguration().db().url();
    private static final String USER = ConfigurationReader.readConfiguration().db().username();
    private static final String PASSWORD = ConfigurationReader.readConfiguration().db().password();

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }
}
