package vasyurin.work.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import vasyurin.work.enams.ProductCategory;
import vasyurin.work.utilites.ConnectionTemplate;
import vasyurin.work.entitys.ProductEntity;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductRepositoryImplPostgres implements ProductRepository {

    @Getter
    private static final ProductRepositoryImplPostgres instance = new ProductRepositoryImplPostgres();

    private ProductRepositoryImplPostgres() {
    }

    @Override
    public void save(ProductEntity product) {
        log.info("Saving product {}", product);

        ProductEntity filter = new ProductEntity();
        filter.setGtin(product.getGtin());

        List<ProductEntity> existingList = getFilteredProducts(filter);

        if (!existingList.isEmpty()) {
            ProductEntity existing = existingList.getFirst();
            product.setId(existing.getId());
            log.info("Updating product {}", product);
            update(product);
        } else {
            log.info(String.format("Saving product %s", product));
            insert(product);
        }
    }

    private void insert(ProductEntity product) {
        log.info("Inserting new product: {}", product);
        String sql = ProductSql.INSERT_PRODUCT;

        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            fillInsertParameters(product, preparedStatement);
            log.info("Executing insert...");
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    product.setId(rs.getInt("id"));
                    log.info("Inserted product id: {}", product.getId());
                }else {
                    log.warn("Insert returned no id!");
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Ошибка вставки продукта", e);

        }
    }

    private void update(ProductEntity product) {
        log.info("InUpdateMethodReposit {}", product);
        String sql = ProductSql.UPDATE_PRODUCT;

        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            fillInsertParameters(product, preparedStatement);
            preparedStatement.setInt(7, product.getId());
            log.info("Executing update...");

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Ошибка обновления продукта", e);
        }
    }

    @Override
    public List<ProductEntity> getAll() {
        List<ProductEntity> products = new ArrayList<>();
        String sql = ProductSql.SELECT_ALL_PRODUCT;

        try (Connection conn = ConnectionTemplate.getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения всех продуктов", e);
        }
        return products;
    }

    @Override
    public List<ProductEntity> getFilteredProducts(ProductEntity filter) {

        String sql = ProductSql.SELECT_FILTER_PRODUCT;

        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            Object[] values = new Object[]{
                    filter.getGtin(), filter.getGtin(),
                    filter.getName(), filter.getName(),
                    filter.getDescription(), filter.getDescription(),
                    filter.getCategory() == null ? null : filter.getCategory().name(),
                    filter.getCategory() == null ? null : filter.getCategory().name(),
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
                    st.setNull(i + 1, types[i]);
                } else {
                    switch (types[i]) {
                        case Types.INTEGER -> st.setInt(i + 1, (Integer) values[i]);
                        case Types.VARCHAR -> st.setString(i + 1, (String) values[i]);
                        case Types.NUMERIC -> st.setBigDecimal(i + 1, (BigDecimal) values[i]);
                        default -> st.setObject(i + 1, values[i]);
                    }
                }
            }

            ResultSet rs = st.executeQuery();
            List<ProductEntity> result = new ArrayList<>();

            while (rs.next()) {
                result.add(mapRow(rs));
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Integer gtin) {
        String sql = ProductSql.DELETE_PRODUCT;

        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, gtin);
            preparedStatement.executeUpdate();

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
                ProductCategory.valueOf(rs.getString("category")), // OK
                rs.getBigDecimal("price"),
                rs.getString("brand")
        );
    }

    private void fillInsertParameters(ProductEntity product, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, product.getGtin());
        preparedStatement.setString(2, product.getName());
        preparedStatement.setString(3, product.getDescription());
        preparedStatement.setString(4, product.getCategory().name());
        preparedStatement.setBigDecimal(5, product.getPrice());
        preparedStatement.setString(6, product.getBrand());
    }

}


