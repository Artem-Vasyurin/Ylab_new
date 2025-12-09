package vasyurin.work.services;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.services.interfases.CacheService;
import vasyurin.work.services.interfases.SaveService;
import vasyurin.work.utilites.ProductMapper;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class SaveServiceImplTest {

    private SaveService saveServiceImpl;
    private ProductRepository productRepositoryMock;
    private ProductMapper mapperMock;
    private CacheService cacheServiceImplMock;

    @BeforeEach
    void setUp() {
        productRepositoryMock = mock(ProductRepository.class);
        mapperMock = mock(ProductMapper.class);
        cacheServiceImplMock = mock(CacheService.class);

        saveServiceImpl = new SaveServiceImpl(productRepositoryMock, mapperMock, cacheServiceImplMock);
    }

    @Test
    @DisplayName("Сохраняем новый продукт")
    void testSave() throws IOException {
        Product dto = Instancio.create(Product.class);
        ProductEntity entity = Instancio.create(ProductEntity.class);

        when(mapperMock.toEntity(dto)).thenReturn(entity);

        saveServiceImpl.save(dto);

        verify(mapperMock).toEntity(dto);
        verify(productRepositoryMock).save(entity);
        verify(cacheServiceImplMock).clear();
    }

    @Test
    @DisplayName("Обновление продукта с валидным GTIN")
    void testUpdate_Success() throws IOException {
        Product dto = Instancio.create(Product.class);

        if (dto.getGtin() == null) {
            dto.setGtin(Instancio.create(Integer.class));
        }

        ProductEntity entity = Instancio.create(ProductEntity.class);

        when(mapperMock.toEntity(dto)).thenReturn(entity);

        saveServiceImpl.update(dto);

        verify(mapperMock).toEntity(dto);
        verify(productRepositoryMock).save(entity);
        verify(cacheServiceImplMock).clear();
    }

    @Test
    @DisplayName("Обновление продукта с null GTIN выбрасывает исключение")
    void testUpdate_GtinNull_ThrowsException() {
        Product dto = Instancio.create(Product.class);
        dto.setGtin(null);

        assertThatThrownBy(() -> saveServiceImpl.update(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("GTIN не может быть null");

        verifyNoInteractions(productRepositoryMock);
        verifyNoInteractions(cacheServiceImplMock);
    }

    @Test
    @DisplayName("Удаление продукта")
    void testDelete() throws IOException {
        Product dto = Instancio.create(Product.class);

        if (dto.getGtin() == null) {
            dto.setGtin(Instancio.create(Integer.class));
        }

        saveServiceImpl.delete(dto);

        verify(productRepositoryMock).delete(dto.getGtin());
        verify(cacheServiceImplMock).clear();
    }
}