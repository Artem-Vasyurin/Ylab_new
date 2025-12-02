package vasyurin.work.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vasyurin.work.enams.ProductCategory;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.utilites.ConnectionTemplate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ProductRepositoryImplPostgres implements ProductRepository {

    private final ConnectionTemplate connectionTemplate;

    public ProductRepositoryImplPostgres(ConnectionTemplate connectionTemplate) {
        this.connectionTemplate = connectionTemplate;
    }

    @Override
    public void save(ProductEntity product) {

        ProductEntity filter = new ProductEntity();
        filter.setGtin(product.getGtin());

        List<ProductEntity> existingList = findFilteredProducts(filter);

        if (!existingList.isEmpty()) {
            ProductEntity first = existingList.get(0);
            product.setId(first.getId());
            update(product);
        } else {
            insert(product);
        }
    }

    private void insert(ProductEntity product) {
        String sql = ProductSql.INSERT_PRODUCT;

        try (Connection conn = connectionTemplate.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            fillInsertParameters(product, preparedStatement);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    product.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Ошибка вставки продукта", e);

        }
    }

    private void update(ProductEntity product) {
        String sql = ProductSql.UPDATE_PRODUCT;

        try (Connection conn = connectionTemplate.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            fillInsertParameters(product, preparedStatement);
            preparedStatement.setInt(7, product.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Ошибка обновления продукта", e);
        }
    }

    @Override
    public List<ProductEntity> findFilteredProducts(ProductEntity filter) {

        String sql = ProductSql.SELECT_FILTER_PRODUCT;

        try (Connection conn = connectionTemplate.getConnection();
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

        try (Connection conn = connectionTemplate.getConnection();
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


