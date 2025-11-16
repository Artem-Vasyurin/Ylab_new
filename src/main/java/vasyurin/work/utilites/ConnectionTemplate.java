package vasyurin.work.utilites;

import vasyurin.work.configurations.ConfigurationReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTemplate {


    private static  String URL = ConfigurationReader.readConfiguration().db().url();
    private static  String USER = ConfigurationReader.readConfiguration().db().username();
    private static  String PASSWORD = ConfigurationReader.readConfiguration().db().password();


    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }
}
