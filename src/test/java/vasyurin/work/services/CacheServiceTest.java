package vasyurin.work.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import vasyurin.work.dto.Product;
import vasyurin.work.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CacheServiceTest {

    private CacheService cacheService;
    private ProductRepository productRepositoryMock;

    private final Product testProduct = new Product(1, "Подушка", "Мягкая", "Дом", 100, "Постелькин");

    @BeforeEach
    void setUp() {
        productRepositoryMock = Mockito.mock(ProductRepository.class);
        cacheService = CacheService.getInstance();
        TestReflectionUtils.setField(cacheService, "productRepository", productRepositoryMock);

        cacheService.clearAllCaches();
    }

    @Test
    void getByName() {
        when(productRepositoryMock.getByName("Подушка")).thenReturn(List.of(testProduct));

        List<Product> firstCall = cacheService.getByName("Подушка");
        List<Product> secondCall = cacheService.getByName("Подушка");

        verify(productRepositoryMock, times(1)).getByName("Подушка");
        assertEquals(firstCall, secondCall);
    }

    @Test
    void getByCategory() {
        when(productRepositoryMock.getByCategory("Дом")).thenReturn(List.of(testProduct));

        cacheService.getByCategory("Дом");
        cacheService.getByCategory("Дом");

        verify(productRepositoryMock, times(1)).getByCategory("Дом");
    }

    @Test
    void getByBrand() {
        when(productRepositoryMock.getByBrand("Постелькин")).thenReturn(List.of(testProduct));

        cacheService.getByBrand("Постелькин");
        cacheService.getByBrand("Постелькин");

        verify(productRepositoryMock, times(1)).getByBrand("Постелькин");
    }

    @Test
    void getByPrice() {
        when(productRepositoryMock.getByPrice(100)).thenReturn(List.of(testProduct));

        cacheService.getByPrice(100);
        cacheService.getByPrice(100);

        verify(productRepositoryMock, times(1)).getByPrice(100);
    }

    @Test
    void getCachedOrFromFile_returnsFromCache() {
        when(productRepositoryMock.getByName("Подушка")).thenReturn(List.of(testProduct));

        cacheService.getByName("Подушка");

        when(productRepositoryMock.getById(1)).thenReturn(Optional.empty());

        Optional<Product> result = cacheService.getCachedOrFromFile(1);

        assertTrue(result.isPresent());
        assertEquals("Подушка", result.get().getName());

        verify(productRepositoryMock, never()).getById(1);
    }


    @Test
    void getCachedOrFromFile_fallsBackToRepository() {
        when(productRepositoryMock.getById(1)).thenReturn(Optional.of(testProduct));

        Optional<Product> result = cacheService.getCachedOrFromFile(1);

        assertTrue(result.isPresent());
        assertEquals("Подушка", result.get().getName());
        verify(productRepositoryMock, times(1)).getById(1);
    }

    @Test
    void clearAllCaches() {
        when(productRepositoryMock.getByName("Подушка")).thenReturn(List.of(testProduct));

        cacheService.getByName("Подушка");
        cacheService.clearAllCaches();
        cacheService.getByName("Подушка");

        verify(productRepositoryMock, times(2)).getByName("Подушка");
    }
}
