package vasyurin.work.repository;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import vasyurin.work.dto.Product;
import vasyurin.work.utilites.ConnectionTemplate;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductRepositoryImplPostgresTest {

    static PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15.4")
            .withDatabaseName("testdb_unit")
            .withUsername("testuser")
            .withPassword("testpass");

    private ProductRepositoryImplPostgres repository;
    private static final String SCHEMA = "app_schema";

    @BeforeAll
    void setUp() throws Exception {
        POSTGRES_CONTAINER.start();

        setPrivateStaticField(ConnectionTemplate.class, "URL", POSTGRES_CONTAINER.getJdbcUrl());
        setPrivateStaticField(ConnectionTemplate.class, "USER", POSTGRES_CONTAINER.getUsername());
        setPrivateStaticField(ConnectionTemplate.class, "PASSWORD", POSTGRES_CONTAINER.getPassword());

        try (Connection conn = POSTGRES_CONTAINER.createConnection("");
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE SCHEMA IF NOT EXISTS " + SCHEMA);
            stmt.execute("CREATE SEQUENCE IF NOT EXISTS " + SCHEMA + ".product_seq START 1");

            stmt.execute("CREATE TABLE IF NOT EXISTS " + SCHEMA + ".products (" +
                    "id BIGINT PRIMARY KEY DEFAULT nextval('" + SCHEMA + ".product_seq')," +
                    "name VARCHAR(255) NOT NULL," +
                    "description TEXT," +
                    "category VARCHAR(255)," +
                    "price INT," +
                    "brand VARCHAR(255)" +
                    ")");
        }

        repository = ProductRepositoryImplPostgres.getInstance();
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (Connection conn = POSTGRES_CONTAINER.createConnection("");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM " + SCHEMA + ".products"); // очищаем данные после каждого теста
        }
    }

    @AfterAll
    void tearDown() {
        POSTGRES_CONTAINER.stop();
    }

    @Test
    @Order(1)
    void saveAndGetById_shouldWork() {
        Product product = new Product(null, "Подушка", "Мягкая", "Постель", 100, "Brand");
        repository.save(product);

        Optional<Product> result = repository.getById(product.getId());
        assertTrue(result.isPresent());
        assertEquals("Подушка", result.get().getName());
    }

    @Test
    @Order(2)
    void update_shouldWork() {
        Product product = new Product(null, "Старое имя", "Описание", "Категория", 50, "Brand");
        repository.save(product);

        product.setName("Новое имя");
        repository.save(product);

        Optional<Product> updated = repository.getById(product.getId());
        assertTrue(updated.isPresent());
        assertEquals("Новое имя", updated.get().getName());
    }

    @Test
    @Order(3)
    void getAll_shouldReturnAllProducts() {
        repository.save(new Product(null, "Продукт1", "Desc1", "Cat1", 10, "Brand1"));
        repository.save(new Product(null, "Продукт2", "Desc2", "Cat2", 20, "Brand2"));

        List<Product> products = repository.getAll();
        assertEquals(2, products.size());
    }

    @Test
    @Order(4)
    void delete_shouldRemoveProduct() {
        Product product = new Product(null, "Удалить", "Desc", "Cat", 10, "Brand");
        repository.save(product);

        repository.delete(product);

        Optional<Product> result = repository.getById(product.getId());
        assertFalse(result.isPresent());
    }

    // Вспомогательный метод для Reflection
    private static void setPrivateStaticField(Class<?> clazz, String fieldName, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }
}
