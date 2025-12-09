package controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.controllers.ProductController;
import vasyurin.work.dto.Product;
import vasyurin.work.enams.ProductCategory;
import vasyurin.work.services.SaveService;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Testcontainers
class ProductControllerTest {

    @Mock
    private SaveService saveService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Product createTestProduct() {
        return new Product(
                123456,
                "Test Product",
                "Описание продукта",
                ProductCategory.ELECTRONICS,
                new BigDecimal("999.99"),
                "TestBrand"
        );
    }

    @Test
    void create_shouldReturnCreatedProduct() throws IOException {
        Product product = createTestProduct();

        ResponseEntity<Product> response = productController.create(product);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());

        verify(saveService, times(1)).save(product);
    }

    @Test
    void delete_shouldReturnNoContent() throws IOException {
        Product product = createTestProduct();

        ResponseEntity<Void> response = productController.delete(product);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(saveService, times(1)).delete(product);
    }

    @Test
    void create_shouldThrowIOException_whenSaveFails() throws IOException {
        Product product = createTestProduct();

        doThrow(new IOException("Ошибка сохранения")).when(saveService).save(product);

        try {
            productController.create(product);
        } catch (IOException e) {
            assertEquals("Ошибка сохранения", e.getMessage());
        }

        verify(saveService, times(1)).save(product);
    }

    @Test
    void delete_shouldThrowIOException_whenDeleteFails() throws IOException {
        Product product = createTestProduct();

        doThrow(new IOException("Ошибка удаления")).when(saveService).delete(product);

        try {
            productController.delete(product);
        } catch (IOException e) {
            assertEquals("Ошибка удаления", e.getMessage());
        }

        verify(saveService, times(1)).delete(product);
    }
}
