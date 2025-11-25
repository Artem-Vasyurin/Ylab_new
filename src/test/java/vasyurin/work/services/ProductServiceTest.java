package vasyurin.work.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.utilites.ProductMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Testcontainers
public class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepositoryMock;
    private ProductMapper mapperMock;
    private CacheService cacheServiceMock;

    @BeforeEach
    void setUp() {
        productService = ProductService.getInstance();

        productRepositoryMock = mock(ProductRepository.class);
        mapperMock = mock(ProductMapper.class);
        cacheServiceMock = mock(CacheService.class);

        TestReflectionUtils.setField(productService, "productRepository", productRepositoryMock);
        TestReflectionUtils.setField(productService, "mapper", mapperMock);
        TestReflectionUtils.setField(productService, "cacheService", cacheServiceMock);
    }

    @Test
    void testGetFilteredProducts_CacheMiss() {
        Product filter = new Product();
        ProductEntity filterEntity = new ProductEntity();

        ProductEntity entity1 = new ProductEntity();
        entity1.setGtin(1);
        Product dto1 = new Product();
        dto1.setGtin(1);

        ProductEntity entity2 = new ProductEntity();
        entity2.setGtin(2);
        Product dto2 = new Product();
        dto2.setGtin(2);

        when(cacheServiceMock.get(filter)).thenReturn(null);
        when(mapperMock.toEntity(filter)).thenReturn(filterEntity);
        when(productRepositoryMock.getFilteredProducts(filterEntity)).thenReturn(List.of(entity1, entity2));
        when(mapperMock.toDto(entity1)).thenReturn(dto1);
        when(mapperMock.toDto(entity2)).thenReturn(dto2);

        List<Product> result = productService.getFilteredProducts(filter);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getGtin());
        assertEquals(2, result.get(1).getGtin());

        verify(cacheServiceMock).put(filter, result);
    }

    @Test
    void testGetFilteredProducts_CacheHit() {
        Product filter = new Product();
        Product cachedProduct = new Product();
        cachedProduct.setGtin(99);

        when(cacheServiceMock.get(filter)).thenReturn(List.of(cachedProduct));

        List<Product> result = productService.getFilteredProducts(filter);

        assertEquals(1, result.size());
        assertEquals(99, result.get(0).getGtin());

        verifyNoInteractions(productRepositoryMock);
        verifyNoInteractions(mapperMock);
    }
}
