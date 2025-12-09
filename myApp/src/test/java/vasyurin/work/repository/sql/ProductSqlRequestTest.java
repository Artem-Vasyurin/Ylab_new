package vasyurin.work.repository.sql;

public class ProductSqlRequestTest {

    public static final String INSERT_PRODUCT = """
        INSERT INTO test_schema.products
        (gtin, name, description, category, price, brand)
        VALUES (?, ?, ?, ?, ?, ?)
        RETURNING id
    """;

    public static final String UPDATE_PRODUCT = """
        UPDATE test_schema.products
        SET gtin = ?, name = ?, description = ?, category = ?, price = ?, brand = ?
        WHERE id = ?
    """;

    public static final String SELECT_FILTER_PRODUCT = """
        SELECT * FROM test_schema.products
        WHERE (? IS NULL OR gtin = ?)
          AND (? IS NULL OR name ILIKE ?)
          AND (? IS NULL OR description ILIKE ?)
          AND (? IS NULL OR category = ?)
          AND (? IS NULL OR price = ?)
          AND (? IS NULL OR brand = ?)
    """;

    public static final String DELETE_PRODUCT = """
        DELETE FROM test_schema.products
        WHERE gtin = ?
    """;

    public static final String CREATE_SCHEME_PRODUCT = """
        CREATE SCHEMA IF NOT EXISTS test_schema
    """;

    public static final String CREATE_TABLE_PRODUCT = """
        CREATE TABLE IF NOT EXISTS test_schema.products (
            id SERIAL PRIMARY KEY,
            gtin INTEGER NOT NULL UNIQUE,
            name VARCHAR(255),
            description TEXT,
            category VARCHAR(50),
            price NUMERIC(10,2),
            brand VARCHAR(255)
        )
    """;

    public static final String TRUNCATE_TABLE_TEST_SCHEMA_PRODUCT = """
        TRUNCATE TABLE test_schema.products RESTART IDENTITY CASCADE
    """;
}