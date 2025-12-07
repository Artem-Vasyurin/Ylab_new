package vasyurin.work.services;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import vasyurin.work.dto.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class ProductValidator {

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
