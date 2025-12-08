package vasyurin.work.services;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vasyurin.work.dto.Product;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CacheServiceImplTest {

    private CacheServiceImpl cacheServiceImpl;

    @BeforeEach
    void setUp() {
        cacheServiceImpl = new CacheServiceImpl();
        cacheServiceImpl.clear();
    }

    @Test
    @DisplayName("Сохраняем и получаем список продуктов из кэша")
    void testPutAndGet() {
        Product key = Instancio.create(Product.class);
        key.setGtin(1);

        Product p1 = Instancio.create(Product.class);
        p1.setGtin(10);
        Product p2 = Instancio.create(Product.class);
        p2.setGtin(20);

        cacheServiceImpl.put(key, List.of(p1, p2));

        List<Product> cached = cacheServiceImpl.get(key);

        assertThat(cached)
                .isNotNull()
                .hasSize(2)
                .extracting(Product::getGtin)
                .containsExactly(10, 20);
    }

    @Test
    @DisplayName("Запрос несуществующего ключа возвращает null")
    void testGet_NonExistingKey() {
        Product key = Instancio.create(Product.class);
        key.setGtin(999);

        List<Product> cached = cacheServiceImpl.get(key);

        assertThat(cached).isNull();
    }

    @Test
    @DisplayName("Очистка кэша удаляет все элементы")
    void testClear() {
        Product key = Instancio.create(Product.class);
        key.setGtin(1);

        Product product = Instancio.create(Product.class);
        cacheServiceImpl.put(key, List.of(product));

        cacheServiceImpl.clear();

        assertThat(cacheServiceImpl.get(key)).isNull();
    }
}
