package vasyurin.work.repository;

import vasyurin.work.dto.Product;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(Product product) throws IOException;

    List<Product> getAll();

    Optional<Product> getById(int id);

    List<Product> getByName(String name);

    List<Product> getByCategory(String category);

    List<Product> getByPrice(int price);

    List<Product> getByBrand(String brand);
    void delete(Product product) throws IOException;




}
