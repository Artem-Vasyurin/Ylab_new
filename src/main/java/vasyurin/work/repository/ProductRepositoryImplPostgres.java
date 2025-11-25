package vasyurin.work.repository;

import lombok.Getter;
import vasyurin.work.dto.Product;
import vasyurin.work.utilites.ConnectionTemplate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImplPostgres implements ProductRepository {

    @Getter
    private static final ProductRepositoryImplPostgres instance = new ProductRepositoryImplPostgres();


    @Override
    public void save(Product product) {
        if (product.getId() == null) {
            insert(product);
        } else {
            update(product);
        }
    }

    private void insert(Product product) {
        String sql = "INSERT INTO app_schema.products (name, description, category, price, brand) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getCategory());
            stmt.setInt(4, product.getPrice());
            stmt.setString(5, product.getBrand());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                product.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка вставки продукта", e);
        }
    }

    private void update(Product product) {
        String sql = "UPDATE app_schema.products SET name=?, description=?, category=?, price=?, brand=? WHERE id=?";
        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getCategory());
            stmt.setInt(4, product.getPrice());
            stmt.setString(5, product.getBrand());
            stmt.setInt(6, product.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления продукта", e);
        }
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM app_schema.products";
        try (Connection conn = ConnectionTemplate.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения всех продуктов", e);
        }
        return products;
    }

    @Override
    public Optional<Product> getById(int id) {
        String sql = "SELECT * FROM app_schema.products WHERE id=?";
        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения продукта по id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> getByName(String name) {
        return getByField("name", name);
    }

    @Override
    public List<Product> getByCategory(String category) {
        return getByField("category", category);
    }

    @Override
    public List<Product> getByBrand(String brand) {
        return getByField("brand", brand);
    }

    @Override
    public List<Product> getByPrice(int price) {
        return getByField("price", price);
    }

    @Override
    public void delete(Product product) {
        String sql = "DELETE FROM app_schema.products WHERE id=?";
        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления продукта", e);
        }
    }

    private List<Product> getByField(String field, Object value) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM app_schema.products WHERE " + field + "=?";
        try (Connection conn = ConnectionTemplate.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (value instanceof String) {
                stmt.setString(1, (String) value);
            } else if (value instanceof Integer) {
                stmt.setInt(1, (Integer) value);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения продуктов по " + field, e);
        }
        return products;
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("category"),
                rs.getInt("price"),
                rs.getString("brand")
        );
    }
}
