package vasyurin.work.repository;

import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.User;
import vasyurin.work.enams.UserRole;
import vasyurin.work.repository.sql.UserSqlRequestTest;
import vasyurin.work.repository.sql.UserSqlTestImpl;
import vasyurin.work.utilites.ConnectionProvider;
import vasyurin.work.utilites.TestConnectionTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryImplPostgresTest {

    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");
    private static final String TEST_SCHEMA = "test_schema";
    private ConnectionProvider testConn;
    private UserRepositoryImplPostgres repository;

    @BeforeAll
    void setup() throws SQLException {
        POSTGRES.start();

        testConn = new TestConnectionTemplate(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
        );

        repository = new UserRepositoryImplPostgres(testConn, new UserSqlTestImpl());

        createSchemaAndTable();
    }

    private void createSchemaAndTable() throws SQLException {
        try (Connection conn = testConn.getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute(UserSqlRequestTest.CREATE_SCHEMA_USERS);
            statement.execute(UserSqlRequestTest.CREATE_TABLE_USERS);
        }
    }

    @BeforeEach
    void cleanTable() throws SQLException {
        try (Connection conn = testConn.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM " + TEST_SCHEMA + ".users");
        }
    }

    @Test
    @DisplayName("Добавление пользователя и проверка")
    void testInsert() {
        User user = Instancio.create(User.class);
        user.setUsername("test");
        user.setPassword("123");
        user.setRole(UserRole.USER);

        repository.save(user);

        Optional<User> found = repository.findByUsername("test");
        assertThat(found).isPresent();
        assertThat(found.get().getPassword()).isEqualTo("123");
        assertThat(found.get().getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("Обновление пользователя")
    void testUpdate() {
        User user = Instancio.create(User.class);
        user.setUsername("john");
        repository.save(user);

        user.setPassword("pass2");
        user.setRole(UserRole.ADMIN);
        repository.save(user);

        Optional<User> found = repository.findByUsername("john");
        assertThat(found).isPresent();
        assertThat(found.get().getPassword()).isEqualTo("pass2");
        assertThat(found.get().getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void testGetAll() {
        User user1 = Instancio.create(User.class);
        user1.setUsername("user1");
        User user2 = Instancio.create(User.class);
        user2.setUsername("user2");

        repository.save(user1);
        repository.save(user2);

        List<User> all = repository.getAll();
        assertThat(all).hasSize(2)
                .extracting(User::getUsername)
                .containsExactlyInAnyOrder("user1", "user2");
    }
}