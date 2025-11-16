package vasyurin.work.repository;

import lombok.Getter;
import vasyurin.work.dto.User;
import vasyurin.work.utilites.ConnectionTemplate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImplPostgres implements UserRepository {

    @Getter
    private static final UserRepositoryImplPostgres instance = new UserRepositoryImplPostgres();

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
        String sql = "INSERT INTO app_schema.users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка вставки пользователя", e);
        }
    }

    private void update(User user) {
        String sql = "UPDATE app_schema.users SET password=?, role=? WHERE username=?";
        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getRole());
            stmt.setString(3, user.getUsername());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления пользователя", e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT username, password, role FROM app_schema.users WHERE username=?";
        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
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
        String sql = "SELECT username, password, role FROM app_schema.users";
        try (Connection conn = ConnectionTemplate.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения всех пользователей", e);
        }
        return users;
    }

}
