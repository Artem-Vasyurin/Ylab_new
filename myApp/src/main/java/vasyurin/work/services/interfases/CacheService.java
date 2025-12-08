package vasyurin.work.services.interfases;

import vasyurin.work.dto.Product;

import java.util.List;

public interface CacheService {
    List<Product> get(Product key);

    void put(Product key, List<Product> products);

    void clear();
}
