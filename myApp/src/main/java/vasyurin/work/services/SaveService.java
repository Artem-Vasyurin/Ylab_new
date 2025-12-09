package vasyurin.work.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vasyurin.work.annotations.AuditAction;
import annotations.LoggingServices;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.utilites.ProductMapper;

import java.io.IOException;

@Slf4j
@Service
public class SaveService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final CacheService cacheService;

    public SaveService(ProductRepository productRepository, ProductMapper mapper, CacheService cacheService) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.cacheService = cacheService;
    }

    @AuditAction
    @LoggingServices
    public void save(Product dto) throws IOException {
        System.out.println("SaveService LOADED BY: " + this.getClass().getClassLoader());
        ProductEntity entity = mapper.toEntity(dto);
        productRepository.save(entity);
        log.trace("Saving product: {}", dto);
        cacheService.clear();
    }

    @AuditAction
    @LoggingServices
    public void update(Product dto) throws IOException {
        if (dto.getGtin() == null) {
            throw new IllegalArgumentException("GTIN не может быть null при обновлении");
        }

        ProductEntity newEntity = mapper.toEntity(dto);

        productRepository.save(newEntity);
        log.trace("Updating product: {}", dto);
        cacheService.clear();

    }

    @AuditAction
    @LoggingServices
    public void delete(Product product) throws IOException {
        productRepository.delete(product.getGtin());
        log.trace("Deleted product: {}", product);
        cacheService.clear();
    }

}

