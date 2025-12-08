package vasyurin.work.repository;

import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.utilites.TestConnectionTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductRepositoryImplPostgresTest {

    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    private static TestConnectionTemplate testConn;
    private ProductRepository repository;

    private static void createSchemaAndTable() throws SQLException {
        try (Connection conn = testConn.getConnection();
             Statement st = conn.createStatement()) {

            st.execute(ProductSqlTest.CREATE_SCHEME_PRODUCT);
            st.execute(ProductSqlTest.CREATE_TABLE_PRODUCT);
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

        repository = new ProductRepositoryForTest(testConn);

        createSchemaAndTable();
    }

    @BeforeEach
    void cleanTable() throws SQLException {
        try (Connection conn = testConn.getConnection();
             Statement st = conn.createStatement()) {
            st.execute(ProductSqlTest.TRUNCATE_TABLE_TEST_SCHEMA_PRODUCT);
        }
    }

    @Test
    @DisplayName("Вставка продукта и проверка наличия")
    void testInsertAndFind() throws IOException {
        ProductEntity product = Instancio.create(ProductEntity.class);
        product.setName("Prod1");
        repository.save(product);

        List<ProductEntity> found = repository.findFilteredProducts(new ProductEntity());
        assertEquals(1, found.size());
        assertEquals("Prod1", found.get(0).getName());
    }

    @Test
    @DisplayName("Обновление продукта и проверка изменений")
    void testUpdate() throws IOException {
        ProductEntity product = Instancio.create(ProductEntity.class);
        product.setName("ProdOld");
        repository.save(product);

        product.setName("ProdNew");
        product.setPrice(BigDecimal.valueOf(15.00));
        repository.save(product);

        List<ProductEntity> found = repository.findFilteredProducts(new ProductEntity());
        assertEquals(1, found.size());
        assertEquals("ProdNew", found.get(0).getName());
        assertEquals(new BigDecimal("15.00"), found.get(0).getPrice());
    }

    @Test
    @DisplayName("Получение всех продуктов")
    void testGetAll() throws IOException {
        ProductEntity product1 = Instancio.create(ProductEntity.class);
        ProductEntity product2 = Instancio.create(ProductEntity.class);
        repository.save(product1);
        repository.save(product2);

        List<ProductEntity> list = repository.findFilteredProducts(new ProductEntity());
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Удаление продукта")
    void testDelete() throws IOException {
        ProductEntity product = Instancio.create(ProductEntity.class);
        product.setGtin(999);
        repository.save(product);

        repository.delete(999);
        List<ProductEntity> list = repository.findFilteredProducts(new ProductEntity());
        assertTrue(list.isEmpty());
    }
}
