package vasyurin.work.repository;

import org.springframework.stereotype.Repository;
import vasyurin.work.dto.User;
import vasyurin.work.utilites.ConnectionTemplate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImplPostgres implements UserRepository {

    private final ConnectionTemplate connectionTemplate;

    public UserRepositoryImplPostgres(ConnectionTemplate connectionTemplate) {
        this.connectionTemplate = connectionTemplate;
    }


    @Override
    public void save(User user) {
        Optional<User> existing = findByUsername(user.getUsername());
        if (existing.isPresent()) {
            update(user);
        } else {
            insert(user);
        }
    }

    private void insert(User user) {
        String sql = UserSql.INSERT_USER;
        try (Connection conn = connectionTemplate.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole());
            preparedStatement.setString(4, user.getToken());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка вставки пользователя", e);
        }
    }

    private void update(User user) {
        String sql = UserSql.UPDATE_USER;
        try (Connection conn = connectionTemplate.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getRole());
            preparedStatement.setString(3, user.getToken());
            preparedStatement.setString(4, user.getUsername());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления пользователя", e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = UserSql.SELECT_BY_USERNAME_USER;
        try (Connection conn = connectionTemplate.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("token")
                );
                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя по username", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = UserSql.SELECT_ALL_USER;
        try (Connection conn = connectionTemplate.getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("token")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения всех пользователей", e);
        }
        return users;
    }
}
