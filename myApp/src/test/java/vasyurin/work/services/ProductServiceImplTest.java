package vasyurin.work.services;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.utilites.ProductMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductServiceImpl productServiceImpl;
    private ProductRepository productRepositoryMock;
    private ProductMapper mapperMock;
    private CacheServiceImpl cacheServiceImplMock;

    @BeforeEach
    void setUp() {
        productRepositoryMock = mock(ProductRepository.class);
        mapperMock = mock(ProductMapper.class);
        cacheServiceImplMock = mock(CacheServiceImpl.class);

        productServiceImpl = new ProductServiceImpl(productRepositoryMock, mapperMock, cacheServiceImplMock);
    }

    @Test
    @DisplayName("Если продукта нет в кэше, возвращаются данные из репозитория и кэш обновляется")
    void testGetFilteredProducts_CacheMiss() {
        Product filter = Instancio.create(Product.class);
        ProductEntity filterEntity = Instancio.create(ProductEntity.class);

        ProductEntity entity1 = Instancio.create(ProductEntity.class);
        entity1.setGtin(1);
        Product dto1 = Instancio.create(Product.class);
        dto1.setGtin(1);

        ProductEntity entity2 = Instancio.create(ProductEntity.class);
        entity2.setGtin(2);
        Product dto2 = Instancio.create(Product.class);
        dto2.setGtin(2);

        when(cacheServiceImplMock.get(filter)).thenReturn(null);
        when(mapperMock.toEntity(filter)).thenReturn(filterEntity);
        when(productRepositoryMock.findFilteredProducts(filterEntity)).thenReturn(List.of(entity1, entity2));
        when(mapperMock.toDto(entity1)).thenReturn(dto1);
        when(mapperMock.toDto(entity2)).thenReturn(dto2);

        List<Product> result = productServiceImpl.getFilteredProducts(filter);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getGtin()).isEqualTo(1);
        assertThat(result.get(1).getGtin()).isEqualTo(2);

        verify(cacheServiceImplMock).put(filter, result);
    }

    @Test
    @DisplayName("Если продукт уже есть в кэше, данные из репозитория не запрашиваются")
    void testGetFilteredProducts_CacheHit() {
        Product filter = Instancio.create(Product.class);
        Product cachedProduct = Instancio.create(Product.class);
        cachedProduct.setGtin(99);

        when(cacheServiceImplMock.get(filter)).thenReturn(List.of(cachedProduct));

        List<Product> result = productServiceImpl.getFilteredProducts(filter);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGtin()).isEqualTo(99);

        verifyNoInteractions(productRepositoryMock);
        verifyNoInteractions(mapperMock);
    }
}
