package vasyurin.work.repository;

import org.junit.jupiter.api.*;
import vasyurin.work.dto.Product;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryImplTest {

    private ProductRepositoryImpl repository;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        repository = ProductRepositoryImpl.getInstance();

        tempFile = File.createTempFile("productsTest", ".txt");
        tempFile.deleteOnExit();

        repository.setFilePathForTest(tempFile.getAbsolutePath());
    }

    @Test
    void saveAndGetById_shouldWorkWithTempFile() throws IOException {
        Product product = new Product(null, "Подушка", "Мягкая", "Постель", 100, "Brand");
        repository.save(product);

        Optional<Product> result = repository.getById(product.getId());
        assertTrue(result.isPresent());
        assertEquals("Подушка", result.get().getName());
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }
}
