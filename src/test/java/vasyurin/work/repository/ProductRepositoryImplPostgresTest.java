package vasyurin.work.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.enams.ProductCategory;
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

    private static final PostgreSQLContainer<?> postgres =
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
            String sql = ProductSqlTest.CREATE_TABLE_PRODUCT;
            st.execute(sql);
        }
    }

    @BeforeAll
    void setup() throws SQLException {
        postgres.start();

        testConn = new TestConnectionTemplate(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
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
    void testInsertAndFind() throws IOException {
        ProductEntity p = new ProductEntity(null, 12345, "Prod1", "Desc1", ProductCategory.ELECTRONICS, BigDecimal.valueOf(10.5), "BrandA");
        repository.save(p);

        List<ProductEntity> found = repository.findFilteredProducts(new ProductEntity());
        assertEquals(1, found.size());
        assertEquals("Prod1", found.get(0).getName());
    }

    @Test
    void testUpdate() throws IOException {
        ProductEntity p = new ProductEntity(null, 11111, "ProdOld", "OldDesc", ProductCategory.ELECTRONICS, BigDecimal.valueOf(5.0), "BrandB");
        repository.save(p);

        ProductEntity updated = new ProductEntity(null, 11111, "ProdNew", "NewDesc", ProductCategory.ELECTRONICS, BigDecimal.valueOf(15.00), "BrandB");
        repository.save(updated);

        List<ProductEntity> found = repository.findFilteredProducts(new ProductEntity());
        assertEquals(1, found.size());
        assertEquals("ProdNew", found.get(0).getName());
        assertEquals(new BigDecimal("15.00"), found.getFirst().getPrice());

    }

    @Test
    void testGetAll() throws IOException {
        repository.save(new ProductEntity(null, 1, "P1", "D1", ProductCategory.ELECTRONICS, BigDecimal.valueOf(100), "BrandX"));
        repository.save(new ProductEntity(null, 2, "P2", "D2", ProductCategory.ELECTRONICS, BigDecimal.valueOf(50), "BrandY"));

        List<ProductEntity> list = repository.findFilteredProducts(new ProductEntity());
        assertEquals(2, list.size());
    }

    @Test
    void testDelete() throws IOException {
        ProductEntity p = new ProductEntity(null, 999, "DelMe", "Desc", ProductCategory.ELECTRONICS, BigDecimal.valueOf(20), "BrandDel");
        repository.save(p);

        repository.delete(999);
        List<ProductEntity> list = repository.findFilteredProducts(new ProductEntity());
        assertTrue(list.isEmpty());
    }
}
