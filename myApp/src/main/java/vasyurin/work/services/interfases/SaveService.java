package vasyurin.work.services.interfases;

import vasyurin.work.dto.Product;

import java.io.IOException;

/**
 * Интерфейс для операций сохранения, обновления и удаления продуктов.
 */
public interface SaveService {
    /**
     * Сохраняет продукт.
     *
     * @param dto продукт для сохранения
     * @throws IOException если произошла ошибка ввода-вывода
     */
    void save(Product dto) throws IOException;

    /**
     * Обновляет продукт.
     *
     * @param dto продукт для обновления
     * @throws IOException если произошла ошибка ввода-вывода
     */
    void update(Product dto) throws IOException;

    /**
     * Удаляет продукт.
     *
     * @param product продукт для удаления
     * @throws IOException если произошла ошибка ввода-вывода
     */
    void delete(Product product) throws IOException;
}