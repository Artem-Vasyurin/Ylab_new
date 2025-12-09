package vasyurin.work.repository;

import vasyurin.work.entitys.ProductEntity;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс для операций с продуктами в хранилище.
 * Определяет методы сохранения, поиска и удаления продуктов.
 */
public interface ProductRepository {
    /**
     * Сохраняет продукт в хранилище.
     *
     * @param product продукт для сохранения
     * @throws IOException если произошла ошибка ввода-вывода
     */
    void save(ProductEntity product) throws IOException;

    /**
     * Возвращает список продуктов, соответствующих заданным критериям фильтрации.
     *
     * @param product объект с фильтром
     * @return список продуктов, удовлетворяющих фильтру
     */

    List<ProductEntity> findFilteredProducts(ProductEntity product);

    /**
     * Удаляет продукт по его GTIN.
     *
     * @param gtin идентификатор продукта
     * @throws IOException если произошла ошибка ввода-вывода
     */
    void delete(Integer gtin) throws IOException;
}
