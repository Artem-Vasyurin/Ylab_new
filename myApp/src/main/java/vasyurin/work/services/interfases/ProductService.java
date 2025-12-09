package vasyurin.work.services.interfases;

import vasyurin.work.dto.Product;

import java.util.List;

/**
 * Интерфейс для работы с продуктами.
 * Определяет контракт получения продуктов по фильтру.
 */
public interface ProductService {
    /**
     * Возвращает список продуктов, соответствующих заданным критериям фильтрации.
     *
     * @param filter объект с критериями фильтрации
     * @return список продуктов, подходящих под фильтр
     */
    List<Product> getFilteredProducts(Product filter);
}