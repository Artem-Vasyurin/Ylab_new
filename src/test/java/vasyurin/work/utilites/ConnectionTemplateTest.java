package vasyurin.work.utilites;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTemplateTest {

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            String url = System.getProperty("DB_URL");
            String user = System.getProperty("DB_USER");
            String password = System.getProperty("DB_PASSWORD");

            if (url == null || user == null || password == null) {
                throw new RuntimeException("System properties DB_URL, DB_USER, DB_PASSWORD должны быть установлены для теста");
            }

            return DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка подключения к тестовой базе данных", e);
        }
    }
}
