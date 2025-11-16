package vasyurin.work.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vasyurin.work.dto.Product;
import vasyurin.work.repository.ProductRepository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveServiceTest {

    private SaveService saveService;
    private ProductRepository productRepository;
    private CacheService cacheService;
    private AuditServiceImpl auditService;

    private Product oldProduct;
    private Product updatedProduct;

    @BeforeEach
    void setUp() {
        saveService = SaveService.getInstance();

        productRepository = mock(ProductRepository.class);
        cacheService = mock(CacheService.class);
        auditService = mock(AuditServiceImpl.class);

        TestReflectionUtils.setField(saveService, "productRepository", productRepository);
        TestReflectionUtils.setField(saveService, "cacheService", cacheService);
        TestReflectionUtils.setField(saveService, "auditService", auditService);

        oldProduct = new Product(1, "Подушка", "Мягкая", "постельное", 100, "Постелькин");
        updatedProduct = new Product(1, "Подушка Deluxe", "очень мягкая", "постельное", 150, "Постелькин");
    }

    @Test
    void getInstance() {
        SaveService instance1 = SaveService.getInstance();
        SaveService instance2 = SaveService.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void save() throws IOException {
        saveService.save(oldProduct);

        verify(productRepository).save(oldProduct);
        verify(cacheService).clearAllCaches();
    }

    @Test
    void update() throws IOException {
        when(cacheService.getCachedOrFromFile(1)).thenReturn(Optional.of(oldProduct));

        saveService.update(updatedProduct);

        verify(productRepository).save(updatedProduct);
        verify(cacheService).clearNameCache();
        verify(cacheService, never()).clearCategoryCache();
    }

    @Test
    void update_notFound() {
        when(cacheService.getCachedOrFromFile(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> saveService.update(updatedProduct));

        try {
            verify(productRepository, never()).save(any());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() throws IOException {
        saveService.delete(oldProduct);

        verify(productRepository).delete(oldProduct);
        verify(cacheService).clearAllCaches();
        verify(auditService).log("Товар удалён");
    }
}
