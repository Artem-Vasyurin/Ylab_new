package vasyurin.work.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import vasyurin.work.dto.User;
import vasyurin.work.enams.UserRole;
import vasyurin.work.repository.sql.UserSqlProvider;
import vasyurin.work.utilites.ConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория пользователей для PostgreSQL.
 * <p>
 * Предоставляет методы для сохранения, обновления, поиска и получения всех пользователей
 * через JDBC с использованием {@link ConnectionProvider}.
 * <p>
 * При сохранении {@link #save(User)} проверяет, существует ли пользователь с таким username,
 * чтобы обновить его данные или вставить новый.
 */
@Repository
@AllArgsConstructor
public class UserRepositoryImplPostgres implements UserRepository {

    private final ConnectionProvider connectionTemplate;
    private final UserSqlProvider sql;

    /**
     * Сохраняет пользователя в базе данных.
     * <p>
     * Если пользователь с указанным username уже существует — обновляет его данные.
     * Иначе вставляет нового пользователя.
     *
     * @param user пользователь для сохранения или обновления
     */
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
        try (Connection conn = connectionTemplate.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.getInsert())) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name());
            ps.setString(4, user.getToken());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка вставки пользователя", e);
        }
    }

    private void update(User user) {
        try (Connection conn = connectionTemplate.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.getUpdate())) {
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getRole().name());
            ps.setString(3, user.getToken());
            ps.setString(4, user.getUsername());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления пользователя", e);
        }
    }

    /**
     * Находит пользователя по username.
     *
     * @param username имя пользователя для поиска
     * @return Optional с найденным пользователем или пустой, если пользователь не найден
     */
    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection conn = connectionTemplate.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.getSelectByUsername())) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role")),
                        rs.getString("token")
                );
                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя по username", e);
        }
        return Optional.empty();
    }

    /**
     * Возвращает список всех пользователей в базе данных.
     *
     * @return список пользователей
     */
    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = connectionTemplate.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.getSelectAll())) {

            while (rs.next()) {
                users.add(new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role")),
                        rs.getString("token")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения всех пользователей", e);
        }
        return users;
    }
}