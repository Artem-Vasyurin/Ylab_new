package vasyurin.work.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class CacheServiceTest {

    private CacheService cacheService;

    public CacheServiceTest(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @BeforeEach
    void setUp() {
        cacheService.clear();
    }

    @Test
    void testPutAndGet() {
        Product key = new Product();
        key.setGtin(1);

        Product p1 = new Product();
        p1.setGtin(10);
        Product p2 = new Product();
        p2.setGtin(20);

        cacheService.put(key, List.of(p1, p2));

        List<Product> cached = cacheService.get(key);

        assertNotNull(cached);
        assertEquals(2, cached.size());
        assertEquals(10, cached.get(0).getGtin());
        assertEquals(20, cached.get(1).getGtin());
    }

    @Test
    void testGet_NonExistingKey() {
        Product key = new Product();
        key.setGtin(999);

        List<Product> cached = cacheService.get(key);
        assertNull(cached);
    }

    @Test
    void testClear() {
        Product key = new Product();
        key.setGtin(1);

        cacheService.put(key, List.of(new Product()));

        cacheService.clear();

        assertNull(cacheService.get(key));
    }
}
