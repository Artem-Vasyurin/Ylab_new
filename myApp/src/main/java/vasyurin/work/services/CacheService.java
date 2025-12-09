package vasyurin.work.services;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import annotations.LoggingServices;
import vasyurin.work.dto.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor
public class CacheService {

    private final Map<Product, List<Product>> cache = new HashMap<>();

    @LoggingServices
    public List<Product> get(Product key) {
        return cache.get(key);
    }

    @LoggingServices
    public void put(Product key, List<Product> products) {
        cache.put(key, products);
    }

    public void clear() {
        cache.clear();
    }

}
