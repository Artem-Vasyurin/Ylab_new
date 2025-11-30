package vasyurin.work.services;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import vasyurin.work.dto.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor
public class CacheService {

    private final Map<Product, List<Product>> cache = new HashMap<>();

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
