package vasyurin.work.services;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import vasyurin.work.dto.Product;
import vasyurin.work.services.interfases.ProductValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация {@link ProductValidator} для проверки корректности данных продукта.
 * <p>
 * Проверяет ограничения по длине названия, описания и бренда,
 * а также положительность цены.
 */
@Component
@NoArgsConstructor
public class ProductValidatorImpl implements ProductValidator {

    /**
     * Валидирует продукт по заданным правилам.
     * <p>
     * Проверяет:
     * <ul>
     *     <li>Название продукта: не более 50 символов</li>
     *     <li>Описание продукта: не более 255 символов</li>
     *     <li>Бренд продукта: не более 50 символов</li>
     *     <li>Цена продукта: не может быть отрицательной</li>
     * </ul>
     *
     * @param product продукт для проверки
     * @return список сообщений об ошибках. Пустой список, если продукт корректен.
     */
    @Override
    public List<String> validate(Product product) {
        List<String> errors = new ArrayList<>();

        if (product.getName() != null && product.getName().length() > 50) {
            errors.add("Название продукта не должно превышать 50 символов");
        }

        if (product.getDescription() != null && product.getDescription().length() > 255) {
            errors.add("Описание продукта не должно превышать 255 символов");
        }

        if (product.getBrand() != null && product.getBrand().length() > 50) {
            errors.add("Бренд продукта не должен превышать 50 символов");
        }

        if (product.getPrice() != null && product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Цена продукта не может быть отрицательной");
        }

        return errors;
    }
}