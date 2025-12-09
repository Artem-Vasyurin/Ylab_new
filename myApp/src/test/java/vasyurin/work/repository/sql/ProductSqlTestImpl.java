package vasyurin.work.repository.sql;

/**
 * Тестовая реализация интерфейса {@link ProductSqlProvider}, предоставляющая SQL-запросы
 * для работы с продуктами в тестовой среде.
 * <p>
 * Этот класс возвращает строки SQL-запросов, определенные в {@link ProductSqlRequestTest}.
 * Используется для тестирования или работы с тестовой базой данных.
 * </p>
 */
public class ProductSqlTestImpl implements ProductSqlProvider {

    /**
     * Возвращает SQL-запрос для вставки нового продукта (тестовый).
     *
     * @return строка SQL для вставки продукта ({@link ProductSqlRequestTest#INSERT_PRODUCT})
     */
    @Override
    public String getInsert() {
        return ProductSqlRequestTest.INSERT_PRODUCT;
    }

    /**
     * Возвращает SQL-запрос для обновления информации о продукте (тестовый).
     *
     * @return строка SQL для обновления продукта ({@link ProductSqlRequestTest#UPDATE_PRODUCT})
     */
    @Override
    public String getUpdate() {
        return ProductSqlRequestTest.UPDATE_PRODUCT;
    }

    /**
     * Возвращает SQL-запрос для выборки продуктов с фильтром (тестовый).
     *
     * @return строка SQL для выборки продуктов с фильтром ({@link ProductSqlRequestTest#SELECT_FILTER_PRODUCT})
     */
    @Override
    public String getSelectFiltered() {
        return ProductSqlRequestTest.SELECT_FILTER_PRODUCT;
    }

    /**
     * Возвращает SQL-запрос для удаления продукта (тестовый).
     *
     * @return строка SQL для удаления продукта ({@link ProductSqlRequestTest#DELETE_PRODUCT})
     */
    @Override
    public String getDelete() {
        return ProductSqlRequestTest.DELETE_PRODUCT;
    }
}