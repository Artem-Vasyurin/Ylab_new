package vasyurin.work.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.utilites.ProductMapper;

import java.io.IOException;

import static org.mockito.Mockito.*;

@Testcontainers
public class SaveServiceTest {

    private SaveService saveService;
    private ProductRepository productRepositoryMock;
    private AuditServiceImpl auditServiceMock;
    private ProductMapper mapperMock;
    private CacheService cacheServiceMock;

    @BeforeEach
    void setUp() {
        saveService = SaveService.getInstance();

        productRepositoryMock = mock(ProductRepository.class);
        auditServiceMock = mock(AuditServiceImpl.class);
        mapperMock = mock(ProductMapper.class);
        cacheServiceMock = mock(CacheService.class);

        TestReflectionUtils.setField(saveService, "productRepository", productRepositoryMock);
        TestReflectionUtils.setField(saveService, "auditService", auditServiceMock);
        TestReflectionUtils.setField(saveService, "mapper", mapperMock);
        TestReflectionUtils.setField(saveService, "cacheService", cacheServiceMock);
    }

    @Test
    void testSave() throws IOException {
        Product dto = new Product();
        dto.setGtin(1);
        ProductEntity entity = new ProductEntity();
        entity.setGtin(1);

        when(mapperMock.toEntity(dto)).thenReturn(entity);

        saveService.save(dto);

        verify(mapperMock).toEntity(dto);
        verify(productRepositoryMock).save(entity);
        verify(cacheServiceMock).clear();
    }

    @Test
    void testUpdate_Success() throws IOException {
        Product dto = new Product();
        dto.setGtin(2);
        ProductEntity entity = new ProductEntity();
        entity.setGtin(2);

        when(mapperMock.toEntity(dto)).thenReturn(entity);

        saveService.update(dto);

        verify(mapperMock).toEntity(dto);
        verify(productRepositoryMock).save(entity);
        verify(cacheServiceMock).clear();
    }

    @Test
    void testUpdate_GtinNull_ThrowsException() {
        Product dto = new Product(); // GTIN null

        try {
            saveService.update(dto);
        } catch (IllegalArgumentException e) {
            assert(e.getMessage().contains("GTIN не может быть null"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        verifyNoInteractions(productRepositoryMock);
        verifyNoInteractions(cacheServiceMock);
    }

    @Test
    void testDelete() throws IOException {
        Product dto = new Product();
        dto.setGtin(3);

        saveService.delete(dto);

        verify(productRepositoryMock).delete(3);
        verify(auditServiceMock).log("Товар удалён GTIN=3");
        verify(cacheServiceMock).clear();
    }
}
