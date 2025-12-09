package vasyurin.work.services.interfases;

import vasyurin.work.dto.Product;

import java.util.List;

/**
 * Сервис кэширования продуктов.
 * <p>Определяет базовые операции получения, добавления и очистки кэша.</p>
 */
public interface CacheService {
    /**
     * Получает список продуктов из кэша по ключу.
     *
     * @param key ключ продукта
     * @return список продуктов, либо пустой список, если ключа нет
     */
    List<Product> get(Product key);

    /**
     * Добавляет список продуктов в кэш по ключу.
     *
     * @param key      ключ продукта
     * @param products список продуктов для кэширования
     */
    void put(Product key, List<Product> products);

    /**
     * Очищает весь кэш.
     */
    void clear();
}