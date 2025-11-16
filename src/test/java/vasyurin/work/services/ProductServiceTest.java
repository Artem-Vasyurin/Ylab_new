package vasyurin.work.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vasyurin.work.dto.Product;
import vasyurin.work.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private CacheService cacheService;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        cacheService = mock(CacheService.class);
        productService = ProductService.getInstance();

        TestReflectionUtils.setField(productService, "productRepository", productRepository);
        TestReflectionUtils.setField(productService, "cacheService", cacheService);;
    }

    @Test
    void getAll() {
        List<Product> expected = List.of(new Product(1, "Машина", "Быстрая", "транспорт", 10_000, "БМВ"));
        when(productRepository.getAll()).thenReturn(expected);

        List<Product> result = productService.getAll();

        assertEquals(expected, result);
        verify(productRepository).getAll();
    }

    @Test
    void getById() {
        Product product = new Product(1, "Машина", "Красивая", "транспорт", 10_000_000, "кёнигсен");
        when(productRepository.getById(1)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getById(1);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productRepository).getById(1);
    }

    @Test
    void getByName() {
        when(cacheService.getByName("Кот")).thenReturn(List.of(new Product()));

        productService.getByName("Кот");

        verify(cacheService).getByName("Кот");
        verifyNoInteractions(productRepository);
    }

    @Test
    void getByCategory() {
        when(cacheService.getByCategory("Одежда")).thenReturn(List.of(new Product()));

        productService.getByCategory("Одежда");

        verify(cacheService).getByCategory("Одежда");
        verifyNoInteractions(productRepository);
    }

    @Test
    void getByBrand() {
        when(cacheService.getByBrand("Гучи")).thenReturn(List.of(new Product()));

        productService.getByBrand("Гучи");

        verify(cacheService).getByBrand("Гучи");
        verifyNoInteractions(productRepository);
    }

    @Test
    void getByPrice() {
        when(cacheService.getByPrice(199)).thenReturn(List.of(new Product()));

        productService.getByPrice(199);

        verify(cacheService).getByPrice(199);
        verifyNoInteractions(productRepository);
    }
}
