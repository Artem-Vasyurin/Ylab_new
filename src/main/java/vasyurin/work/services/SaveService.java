package vasyurin.work.services;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import vasyurin.work.annotations.AuditAction;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.repository.ProductRepositoryImplPostgres;
import vasyurin.work.utilites.ProductMapper;

import java.io.IOException;

@Slf4j
public class SaveService {

    @Getter
    private static final SaveService instance = new SaveService();

    private final ProductRepository productRepository;
    private final AuditServiceImpl auditService;
    private final ProductMapper mapper = ProductMapper.INSTANCE;
    private final CacheService cacheService;

    private SaveService() {
        this.productRepository = ProductRepositoryImplPostgres.getInstance();
        this.auditService = AuditServiceImpl.getInstance();
        this.cacheService = CacheService.getInstance();
    }

    @AuditAction
    public void save(Product dto) throws IOException {
        System.out.println("SaveService LOADED BY: " + this.getClass().getClassLoader());
        log.info("Saving product: {}", dto);
        ProductEntity entity = mapper.toEntity(dto);
        productRepository.save(entity);
        cacheService.clear();
    }

    public void update(Product dto) throws IOException {
        if (dto.getGtin() == null) {
            throw new IllegalArgumentException("GTIN не может быть null при обновлении");
        }

        ProductEntity newEntity = mapper.toEntity(dto);

        productRepository.save(newEntity);
        cacheService.clear();

    }

    @AuditAction
    public void delete(Product product) throws IOException {
        productRepository.delete(product.getGtin());
        auditService.log("Товар удалён GTIN=" + product.getGtin());
        cacheService.clear();
    }

}

