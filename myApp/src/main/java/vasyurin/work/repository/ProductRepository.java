package vasyurin.work.repository;

import vasyurin.work.entitys.ProductEntity;

import java.io.IOException;
import java.util.List;

public interface ProductRepository {
    void save(ProductEntity product) throws IOException;


    List<ProductEntity> findFilteredProducts(ProductEntity product);

    void delete(Integer gtin) throws IOException;
}
