package vasyurin.work.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.Product;
import vasyurin.work.services.ProductService;
import vasyurin.work.services.ProductValidator;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Testcontainers
class CatalogControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductValidator validator;

    @InjectMocks
    private CatalogController catalogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Product createTestProduct() {
        return new Product(
                123456,
                "Блузка",
                "Удобная блузка",
                null,
                new BigDecimal("999.99"),
                "TestBrand"
        );
    }

    @Test
    void getCatalog_shouldReturnWorkingMessage() {
        String response = catalogController.getCatalog();
        assertEquals("Catalog is working!", response);
    }

    @Test
    void filter_shouldReturnValidationErrors_whenProductInvalid() {
        Product filter = createTestProduct();
        List<String> errors = List.of("Имя не может быть пустым!", "Цена должна быть положительной");

        when(validator.validate(filter)).thenReturn(errors);

        Object response = catalogController.filter(filter);

        assertEquals(errors, ((CatalogController.ErrorResponse) response).validationErrors());

        verify(validator, times(1)).validate(filter);
        verifyNoInteractions(productService);
    }

    @Test
    void filter_shouldReturnFilteredProducts_whenProductValid() {
        Product filter = createTestProduct();
        List<String> noErrors = List.of();
        List<Product> filteredProducts = List.of(filter);

        when(validator.validate(filter)).thenReturn(noErrors);
        when(productService.getFilteredProducts(filter)).thenReturn(filteredProducts);

        Object response = catalogController.filter(filter);

        assertEquals(filteredProducts, response);

        verify(validator, times(1)).validate(filter);
        verify(productService, times(1)).getFilteredProducts(filter);
    }
}
