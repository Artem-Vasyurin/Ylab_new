package vasyurin.work.services;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.Product;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class ProductValidatorTest {

    private final ProductValidator validator;

    public ProductValidatorTest(ProductValidator validator) {
        this.validator = validator;
    }

    @Test
    void testValidProduct_NoErrors() {
        Product product = new Product();
        product.setName("Valid Name");
        product.setDescription("Valid description");
        product.setBrand("ValidBrand");
        product.setPrice(BigDecimal.valueOf(100));

        List<String> errors = validator.validate(product);
        assertTrue(errors.isEmpty(), "Ожидается, что ошибок нет");
    }

    @Test
    void testNameTooLong() {
        Product product = new Product();
        product.setName("A".repeat(51));

        List<String> errors = validator.validate(product);
        assertEquals(1, errors.size());
        assertEquals("Название продукта не должно превышать 50 символов", errors.get(0));
    }

    @Test
    void testDescriptionTooLong() {
        Product product = new Product();
        product.setDescription("D".repeat(256));

        List<String> errors = validator.validate(product);
        assertEquals(1, errors.size());
        assertEquals("Описание продукта не должно превышать 255 символов", errors.get(0));
    }

    @Test
    void testBrandTooLong() {
        Product product = new Product();
        product.setBrand("B".repeat(51));

        List<String> errors = validator.validate(product);
        assertEquals(1, errors.size());
        assertEquals("Бренд продукта не должен превышать 50 символов", errors.get(0));
    }

    @Test
    void testNegativePrice() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-10));

        List<String> errors = validator.validate(product);
        assertEquals(1, errors.size());
        assertEquals("Цена продукта не может быть отрицательной", errors.get(0));
    }

    @Test
    void testMultipleErrors() {
        Product product = new Product();
        product.setName("A".repeat(51));
        product.setDescription("D".repeat(256));
        product.setBrand("B".repeat(51));
        product.setPrice(BigDecimal.valueOf(-5));

        List<String> errors = validator.validate(product);
        assertEquals(4, errors.size());
        assertTrue(errors.contains("Название продукта не должно превышать 50 символов"));
        assertTrue(errors.contains("Описание продукта не должно превышать 255 символов"));
        assertTrue(errors.contains("Бренд продукта не должен превышать 50 символов"));
        assertTrue(errors.contains("Цена продукта не может быть отрицательной"));
    }
}
