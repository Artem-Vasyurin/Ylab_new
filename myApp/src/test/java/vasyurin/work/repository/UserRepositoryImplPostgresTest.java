package vasyurin.work.repository;

import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.utilites.TestConnectionTemplate;
import vasyurin.work.dto.User;
import vasyurin.work.enams.UserRole;

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

    private static TestConnectionTemplate testConn;
    private UserRepository repository;

    private static void createSchemaAndTables() throws SQLException {
        try (Connection connection = testConn.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(UserSqlTest.CREATE_SCHEMA_USERS);
            statement.execute(UserSqlTest.CREATE_TABLE_USERS);
        }
    }

    @BeforeAll
    void setup() throws SQLException {
        POSTGRES.start();

        testConn = new TestConnectionTemplate(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
        );

        repository = new UserRepositoryForTest(testConn);

        createSchemaAndTables();
    }

    @BeforeEach
    void cleanTable() throws SQLException {
        try (Connection conn = testConn.getConnection();
             Statement st = conn.createStatement()) {

            st.execute(UserSqlTest.DELETE_TABLE_USERS);
        }
    }

    @Test
    @DisplayName("Добавление пользователя и проверка существования")
    void testInsert() throws Exception {
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
    @DisplayName("Обновление существующего пользователя и проверка изменений")
    void testUpdate() throws Exception {
        User user = Instancio.create(User.class);
        repository.save(user);

        user.setUsername("john");
        user.setPassword("pass2");
        user.setRole(UserRole.ADMIN);
        repository.save(user);

        var found = repository.findByUsername("john");

        assertThat(found).isPresent();
        assertThat(found.get().getPassword()).isEqualTo("pass2");
        assertThat(found.get().getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void testGetAll() throws Exception {
        User user1 = Instancio.create(User.class);
        user1.setUsername("user1");
        User user2 = Instancio.create(User.class);
        user2.setUsername("user2");

        repository.save(user1);
        repository.save(user2);

        List<User> allUsers = repository.getAll();

        assertThat(allUsers).hasSize(2)
                .extracting(User::getUsername)
                .containsExactlyInAnyOrder("user1", "user2");
    }

}
