package vasyurin.work.repository.sql;

/**
 * Интерфейс для предоставления SQL-запросов для работы с продуктами.
 * Каждый метод возвращает SQL-запрос соответствующего типа.
 */
public interface ProductSqlProvider {
    /**
     * Возвращает SQL-запрос для вставки нового продукта в базу данных.
     *
     * @return SQL-запрос INSERT
     */
    String getInsert();

    /**
     * Возвращает SQL-запрос для обновления существующего продукта.
     *
     * @return SQL-запрос UPDATE
     */
    String getUpdate();

    /**
     * Возвращает SQL-запрос для выборки продуктов с фильтром.
     *
     * @return SQL-запрос SELECT с фильтром
     */
    String getSelectFiltered();

    /**
     * Возвращает SQL-запрос для удаления продукта.
     *
     * @return SQL-запрос DELETE
     */
    String getDelete();
}