package vasyurin.work.repository.sql;

import org.springframework.stereotype.Service;

/**
 * Реализация {@link ProductSqlProvider}, которая предоставляет SQL-запросы
 * для работы с продуктами в продуктивной среде.
 */
@Service
public class ProductSqlProd implements ProductSqlProvider {
    /**
     * Возвращает SQL-запрос для вставки нового продукта.
     */
    @Override
    public String getInsert() {
        return ProductSqlRequest.INSERT_PRODUCT;
    }

    /**
     * Возвращает SQL-запрос для обновления продукта.
     */
    @Override
    public String getUpdate() {
        return ProductSqlRequest.UPDATE_PRODUCT;
    }

    /**
     * Возвращает SQL-запрос для выборки продуктов с фильтром.
     */
    @Override
    public String getSelectFiltered() {
        return ProductSqlRequest.SELECT_FILTER_PRODUCT;
    }

    /**
     * Возвращает SQL-запрос для удаления продукта.
     */
    @Override
    public String getDelete() {
        return ProductSqlRequest.DELETE_PRODUCT;
    }
}