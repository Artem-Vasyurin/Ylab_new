package vasyurin.work.services.interfases;

import vasyurin.work.dto.Product;

import java.util.List;

/**
 * Интерфейс для валидации продуктов.
 * Определяет метод проверки продукта и возвращения списка ошибок валидации.
 */
public interface ProductValidator {
    /**
     * Проверяет продукт на соответствие правилам валидации.
     *
     * @param product продукт для проверки
     * @return список сообщений об ошибках валидации; пустой список, если ошибок нет
     */
    List<String> validate(Product product);
}
