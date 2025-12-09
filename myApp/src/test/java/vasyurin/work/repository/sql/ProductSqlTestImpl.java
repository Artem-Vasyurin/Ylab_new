package vasyurin.work.repository.sql;

import org.springframework.stereotype.Service;

/**
 * Тестовая реализация {@link ProductSqlProvider}, предоставляющая SQL-запросы
 * для работы с продуктами в тестовой среде.
 */
@Service
public class ProductSqlTestImpl implements ProductSqlProvider {

    /**
     * Возвращает SQL-запрос для вставки нового продукта (тестовый).
     */
    @Override
    public String getInsert() {
        return ProductSqlRequestTest.INSERT_PRODUCT;
    }

    /**
     * Возвращает SQL-запрос для обновления продукта (тестовый).
     */
    @Override
    public String getUpdate() {
        return ProductSqlRequestTest.UPDATE_PRODUCT;
    }

    /**
     * Возвращает SQL-запрос для выборки продуктов с фильтром (тестовый).
     */
    @Override
    public String getSelectFiltered() {
        return ProductSqlRequestTest.SELECT_FILTER_PRODUCT;
    }

    /**
     * Возвращает SQL-запрос для удаления продукта (тестовый).
     */
    @Override
    public String getDelete() {
        return ProductSqlRequestTest.DELETE_PRODUCT;
    }
}
