package vasyurin.work.services;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vasyurin.work.dto.Product;
import vasyurin.work.services.interfases.ProductValidator;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;


class ProductValidatorImplTest {

    private final ProductValidator validator = new ProductValidatorImpl();

    @Test
    @DisplayName("Валидный продукт не выдаёт ошибок")
    void testValidProduct_NoErrors() {
        Product product = Instancio.of(Product.class)
                .set(field("name"), "Valid Name")
                .set(field("description"), "Valid description")
                .set(field("brand"), "ValidBrand")
                .set(field("price"), BigDecimal.valueOf(100))
                .create();

        List<String> errors = validator.validate(product);
        assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("Название продукта слишком длинное")
    void testNameTooLong() {
        Product product = Instancio.of(Product.class)
                .set(field("name"), "A".repeat(51))
                .create();

        List<String> errors = validator.validate(product);
        assertThat(errors).containsExactly("Название продукта не должно превышать 50 символов");
    }

    @Test
    @DisplayName("Описание продукта слишком длинное")
    void testDescriptionTooLong() {
        Product product = Instancio.of(Product.class)
                .set(field("description"), "D".repeat(256))
                .create();

        List<String> errors = validator.validate(product);
        assertThat(errors).containsExactly("Описание продукта не должно превышать 255 символов");
    }

    @Test
    @DisplayName("Бренд продукта слишком длинный")
    void testBrandTooLong() {
        Product product = Instancio.of(Product.class)
                .set(field("brand"), "B".repeat(51))
                .create();

        List<String> errors = validator.validate(product);
        assertThat(errors).containsExactly("Бренд продукта не должен превышать 50 символов");
    }

    @Test
    @DisplayName("Цена продукта отрицательная")
    void testNegativePrice() {
        Product product = Instancio.of(Product.class)
                .set(field("price"), BigDecimal.valueOf(-10))
                .create();

        List<String> errors = validator.validate(product);
        assertThat(errors).containsExactly("Цена продукта не может быть отрицательной");
    }

    @Test
    @DisplayName("Множественные ошибки одновременно")
    void testMultipleErrors() {
        Product product = Instancio.of(Product.class)
                .set(field("name"), "A".repeat(51))
                .set(field("description"), "D".repeat(256))
                .set(field("brand"), "B".repeat(51))
                .set(field("price"), BigDecimal.valueOf(-5))
                .create();

        List<String> errors = validator.validate(product);
        assertThat(errors).containsExactlyInAnyOrder(
                "Название продукта не должно превышать 50 символов",
                "Описание продукта не должно превышать 255 символов",
                "Бренд продукта не должен превышать 50 символов",
                "Цена продукта не может быть отрицательной"
        );
    }
}