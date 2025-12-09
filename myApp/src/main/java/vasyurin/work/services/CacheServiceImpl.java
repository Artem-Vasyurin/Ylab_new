package vasyurin.work.services;

import loggermetricksaspect.annotations.LoggingServices;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import vasyurin.work.dto.Product;
import vasyurin.work.services.interfases.CacheService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Кеш сервис для хранения результатов поиска продуктов.
 * <p>
 * Позволяет сохранять и получать список продуктов по ключу {@link Product}.
 * Используется в памяти через {@link HashMap}.
 */
@Service
@NoArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final Map<Product, List<Product>> cache = new HashMap<>();

    /**
     * Возвращает список продуктов из кэша по ключу.
     *
     * @param key продукт, по которому ищется список в кэше
     * @return список продуктов или null, если ключа нет в кэше
     */
    @LoggingServices
    @Override
    public List<Product> get(Product key) {
        return cache.get(key);
    }

    /**
     * Сохраняет список продуктов в кэш по ключу.
     *
     * @param key      продукт, который используется как ключ
     * @param products список продуктов для сохранения в кэше
     */
    @LoggingServices
    @Override
    public void put(Product key, List<Product> products) {
        cache.put(key, products);
    }

    /**
     * Очищает весь кэш.
     */
    @LoggingServices
    public void clear() {
        cache.clear();
    }

}