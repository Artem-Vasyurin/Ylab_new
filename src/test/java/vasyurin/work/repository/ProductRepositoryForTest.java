package vasyurin.work.repository;

import vasyurin.work.enams.ProductCategory;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.utilites.TestConnectionTemplate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryForTest implements ProductRepository {

    private final TestConnectionTemplate connection;

    public ProductRepositoryForTest(TestConnectionTemplate connection) {
        this.connection = connection;
    }

    @Override
    public void save(ProductEntity product) {

        ProductEntity filter = new ProductEntity();
        filter.setGtin(product.getGtin());

        List<ProductEntity> existingList = findFilteredProducts(filter);

        if (!existingList.isEmpty()) {
            ProductEntity existing = existingList.getFirst();
            product.setId(existing.getId());
            update(product);
        } else {
            insert(product);
        }
    }

    private void insert(ProductEntity product) {
        try (Connection conn = connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(ProductSqlTest.INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {

            fillParameters(ps, product);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка вставки продукта", e);
        }
    }

    private void update(ProductEntity product) {
        try (Connection conn = connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(ProductSqlTest.UPDATE_PRODUCT)) {

            fillParameters(ps, product);
            ps.setInt(7, product.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления продукта", e);
        }
    }

    private void fillParameters(PreparedStatement ps, ProductEntity product) throws SQLException {
        ps.setInt(1, product.getGtin());
        ps.setString(2, product.getName());
        ps.setString(3, product.getDescription());
        ps.setString(4, product.getCategory() != null ? product.getCategory().name() : null);
        ps.setBigDecimal(5, product.getPrice());
        ps.setString(6, product.getBrand());
    }

    @Override
    public List<ProductEntity> findFilteredProducts(ProductEntity filter) {
        List<ProductEntity> list = new ArrayList<>();

        try (Connection conn = connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(ProductSqlTest.SELECT_FILTER_PRODUCT)) {

            Object[] values = new Object[]{
                    filter.getGtin(), filter.getGtin(),
                    filter.getName(), filter.getName(),
                    filter.getDescription(), filter.getDescription(),
                    filter.getCategory() != null ? filter.getCategory().name() : null,
                    filter.getCategory() != null ? filter.getCategory().name() : null,
                    filter.getPrice(), filter.getPrice(),
                    filter.getBrand(), filter.getBrand()
            };

            int[] types = new int[]{
                    Types.INTEGER, Types.INTEGER,
                    Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR,
                    Types.NUMERIC, Types.NUMERIC,
                    Types.VARCHAR, Types.VARCHAR
            };

            for (int i = 0; i < values.length; i++) {
                if (values[i] == null) {
                    ps.setNull(i + 1, types[i]);
                } else {
                    switch (types[i]) {
                        case Types.INTEGER -> ps.setInt(i + 1, (Integer) values[i]);
                        case Types.VARCHAR -> ps.setString(i + 1, (String) values[i]);
                        case Types.NUMERIC -> ps.setBigDecimal(i + 1, (BigDecimal) values[i]);
                        default -> ps.setObject(i + 1, values[i]);
                    }
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска продуктов по фильтру", e);
        }

        return list;
    }

    @Override
    public void delete(Integer gtin) {
        try (Connection conn = connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(ProductSqlTest.DELETE_PRODUCT)) {

            ps.setInt(1, gtin);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления продукта по GTIN", e);
        }
    }

    private ProductEntity mapRow(ResultSet rs) throws SQLException {
        return new ProductEntity(
                rs.getInt("id"),
                rs.getInt("gtin"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("category") != null ? ProductCategory.valueOf(rs.getString("category")) : null,
                rs.getBigDecimal("price"),
                rs.getString("brand")
        );
    }


}
