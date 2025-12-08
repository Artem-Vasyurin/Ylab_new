package vasyurin.work.services;

import annotations.LoggingServices;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import vasyurin.work.dto.Product;
import vasyurin.work.services.interfases.CacheService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final Map<Product, List<Product>> cache = new HashMap<>();

    @LoggingServices
    @Override
    public List<Product> get(Product key) {
        return cache.get(key);
    }

    @LoggingServices
    @Override
    public void put(Product key, List<Product> products) {
        cache.put(key, products);
    }

    @LoggingServices
    public void clear() {
        cache.clear();
    }

}
