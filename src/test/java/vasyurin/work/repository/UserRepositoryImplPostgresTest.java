package vasyurin.work.repository;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import vasyurin.work.dto.User;
import vasyurin.work.utilites.ConnectionTemplate;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryImplPostgresTest {

    static PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15.4")
            .withDatabaseName("testdb_users")
            .withUsername("testuser")
            .withPassword("testpass");

    private static final String SCHEMA = "app_schema";
    private UserRepositoryImplPostgres repository;

    @BeforeAll
    void setUp() throws Exception {
        POSTGRES.start();

        setPrivateStaticField(ConnectionTemplate.class, "URL", POSTGRES.getJdbcUrl());
        setPrivateStaticField(ConnectionTemplate.class, "USER", POSTGRES.getUsername());
        setPrivateStaticField(ConnectionTemplate.class, "PASSWORD", POSTGRES.getPassword());

        try (Connection conn = POSTGRES.createConnection("");
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE SCHEMA IF NOT EXISTS " + SCHEMA);
            stmt.execute("CREATE TABLE IF NOT EXISTS " + SCHEMA + ".users (" +
                    "username VARCHAR(255) PRIMARY KEY," +
                    "password VARCHAR(255) NOT NULL," +
                    "role VARCHAR(64) NOT NULL" +
                    ")");
        }

        repository = UserRepositoryImplPostgres.getInstance();
    }

    @AfterEach
    void clean() throws Exception {
        try (Connection conn = POSTGRES.createConnection("");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM " + SCHEMA + ".users");
        }
    }

    @AfterAll
    void tearDown() {
        POSTGRES.stop();
    }

    @Test
    @Order(1)
    void saveAndFind_shouldWork() {
        User u = new User("ivan", "111", "ADMIN");
        repository.save(u);

        Optional<User> found = repository.findByUsername("ivan");

        assertTrue(found.isPresent());
        assertEquals("111", found.get().getPassword());
        assertEquals("ADMIN", found.get().getRole());
    }

    @Test
    @Order(2)
    void update_shouldWork() {
        User u = new User("kate", "pwd", "USER");
        repository.save(u);

        u.setPassword("new");
        u.setRole("ADMIN");
        repository.save(u);

        Optional<User> found = repository.findByUsername("kate");

        assertTrue(found.isPresent());
        assertEquals("new", found.get().getPassword());
        assertEquals("ADMIN", found.get().getRole());
    }

    @Test
    @Order(3)
    void getAll_shouldReturnAll() {
        repository.save(new User("u1", "1", "USER"));
        repository.save(new User("u2", "2", "ADMIN"));

        List<User> all = repository.getAll();

        assertEquals(2, all.size());
    }

    private static void setPrivateStaticField(Class<?> clazz, String name, Object value) throws Exception {
        Field f = clazz.getDeclaredField(name);
        f.setAccessible(true);
        f.set(null, value);
    }
}
