//package vasyurin.work.repository;
//
//import vasyurin.work.dto.User;
//import utilites.TestConnectionTemplate;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class UserRepositoryForTest implements UserRepository {
//
//    private final TestConnectionTemplate connection;
//    private final List<User> users = new ArrayList<>();
//
//    public UserRepositoryForTest(TestConnectionTemplate connection) {
//        this.connection = connection;
//    }
//
//    @Override
//    public void save(User user) {
//        Optional<User> existing = findByUsername(user.getUsername());
//        if (existing.isPresent()) {
//            update(user);
//        } else {
//            insert(user);
//        }
//    }
//
//    private void insert(User user) {
//        try (Connection conn = connection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(UserSqlTest.INSERT_USER)) {
//
//            ps.setString(1, user.getUsername());
//            ps.setString(2, user.getPassword());
//            ps.setString(3, user.getRole());
//            ps.setString(4, user.getToken());
//            ps.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void update(User user) {
//        try (Connection conn = connection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(UserSqlTest.UPDATE_USER)) {
//
//            ps.setString(1, user.getPassword());
//            ps.setString(2, user.getRole());
//            ps.setString(3, user.getToken());
//            ps.setString(4, user.getUsername());
//            ps.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public Optional<User> findByUsername(String username) {
//        try (Connection conn = connection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(UserSqlTest.SELECT_BY_USERNAME_USER)) {
//
//            ps.setString(1, username);
//
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return Optional.of(new User(
//                        rs.getString("username"),
//                        rs.getString("password"),
//                        rs.getString("role"),
//                        rs.getString("token")
//                ));
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        return Optional.empty();
//    }
//
//    @Override
//    public Optional<User> findByToken(String token) {
//        return users.stream()
//                .filter(u -> token.equals(u.getToken()))
//                .findFirst();
//    }
//
//    @Override
//    public List<User> getAll() {
//        List<User> list = new ArrayList<>();
//
//        try (Connection conn = connection.getConnection();
//             Statement st = conn.createStatement();
//             ResultSet rs = st.executeQuery(UserSqlTest.SELECT_ALL_USER)) {
//
//            while (rs.next()) {
//                list.add(new User(
//                        rs.getString("username"),
//                        rs.getString("password"),
//                        rs.getString("role"),
//                        rs.getString("token")
//                ));
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        return list;
//    }
//}
