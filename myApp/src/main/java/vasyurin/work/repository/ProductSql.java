package vasyurin.work.repository;

public class ProductSql {

    public static final String INSERT_PRODUCT = """
        INSERT INTO app_schema.products (gtin, name, description, category, price, brand)
        VALUES (?, ?, ?, ?, ?, ?)
        RETURNING id
        """;

    public static final String UPDATE_PRODUCT = """
                UPDATE app_schema.products SET
                    gtin=?, name=?, description=?, category=?, price=?, brand=?
                WHERE id=?
                """;

    public static final String SELECT_FILTER_PRODUCT = """
        SELECT * FROM app_schema.products
        WHERE (? IS NULL OR gtin = ?)
          AND (? IS NULL OR LOWER(name) = LOWER(?))
          AND (? IS NULL OR LOWER(description) = LOWER(?))
          AND (? IS NULL OR category = ?)
          AND (? IS NULL OR price = ?)
          AND (? IS NULL OR LOWER(brand) = LOWER(?))
    """;

    public static final String DELETE_PRODUCT = """
            DELETE FROM app_schema.products WHERE gtin=?
            """;
}
