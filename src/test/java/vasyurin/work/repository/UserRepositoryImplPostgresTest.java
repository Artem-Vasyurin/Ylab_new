package vasyurin.work.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.User;
import vasyurin.work.utilites.TestConnectionTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryImplPostgresTest {

    private static final PostgreSQLContainer<?> postgres =
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

            String sql = UserSqlTest.CREATE_TABLE_USERS;
            statement.execute(sql);
        }
    }

    @BeforeAll
    void setup() throws Exception {
        postgres.start();

        testConn = new TestConnectionTemplate(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        repository = new UserRepositoryForTest(testConn);

        createSchemaAndTables();
    }

    @BeforeEach
    void cleanTable() throws Exception {
        try (Connection conn = testConn.getConnection();
             Statement st = conn.createStatement()) {

            st.execute(UserSqlTest.DELETE_TABELE_USERS);
        }
    }

    @Test
    void testInsert() throws Exception {
        User u = new User("test", "123", "USER", "t1");
        repository.save(u);

        var found = repository.findByUsername("test");

        assertTrue(found.isPresent());
        assertEquals("123", found.get().getPassword());
    }

    @Test
    void testUpdate() throws Exception {
        User u = new User("john", "pass1", "USER", "tok1");
        repository.save(u);

        User u2 = new User("john", "pass2", "ADMIN", "tok2");
        repository.save(u2);

        var f = repository.findByUsername("john");

        assertTrue(f.isPresent());
        assertEquals("pass2", f.get().getPassword());
        assertEquals("ADMIN", f.get().getRole());
    }

    @Test
    void testGetAll() throws Exception {
        repository.save(new User("u1", "p1", "R1", "t1"));
        repository.save(new User("u2", "p2", "R2", "t2"));

        List<User> list = repository.getAll();

        assertEquals(2, list.size());
    }
}
