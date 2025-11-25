package vasyurin.work.repository;

import vasyurin.work.entitys.ProductEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(ProductEntity product) throws IOException;

    List<ProductEntity> getAll();

    List<ProductEntity> getFilteredProducts(ProductEntity product);

    void delete(Integer gtin) throws IOException;
}
