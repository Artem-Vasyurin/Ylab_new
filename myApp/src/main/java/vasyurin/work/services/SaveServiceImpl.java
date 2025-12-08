package vasyurin.work.services;

import annotations.LoggingServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.services.interfases.SaveService;
import vasyurin.work.utilites.ProductMapper;

import java.io.IOException;

@Slf4j
@Service
public class SaveServiceImpl implements SaveService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final CacheServiceImpl cacheServiceImpl;

    public SaveServiceImpl(ProductRepository productRepository, ProductMapper mapper, CacheServiceImpl cacheServiceImpl) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.cacheServiceImpl = cacheServiceImpl;
    }

    @LoggingServices
    @Override
    public void save(Product dto) throws IOException {
        ProductEntity entity = mapper.toEntity(dto);
        productRepository.save(entity);
        log.trace("Saving product: {}", dto);
        cacheServiceImpl.clear();
    }

    @LoggingServices
    @Override
    public void update(Product dto) throws IOException {
        if (dto.getGtin() == null) {
            throw new IllegalArgumentException("GTIN не может быть null при обновлении");
        }

        ProductEntity newEntity = mapper.toEntity(dto);

        productRepository.save(newEntity);
        log.trace("Updating product: {}", dto);
        cacheServiceImpl.clear();
    }

    @LoggingServices
    @Override
    public void delete(Product product) throws IOException {
        productRepository.delete(product.getGtin());
        log.trace("Deleted product: {}", product);
        cacheServiceImpl.clear();
    }
}

