package vasyurin.work.services;

import lombok.Getter;
import vasyurin.work.dto.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheService {

    @Getter
    private static final CacheService instance = new CacheService();

    private final Map<Product, List<Product>> cache = new HashMap<>();

    private CacheService() {
    }

    public List<Product> get(Product key) {
        return cache.get(key);
    }

    public void put(Product key, List<Product> products) {
        cache.put(key, products);
    }

    public void clear() {
        cache.clear();
    }

}
