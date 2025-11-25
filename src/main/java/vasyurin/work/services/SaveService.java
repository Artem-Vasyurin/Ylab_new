package vasyurin.work.services;

import lombok.Getter;
import vasyurin.work.dto.Product;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.repository.ProductRepositoryImpl;
import vasyurin.work.repository.ProductRepositoryImplPostgres;

import java.io.IOException;
import java.util.Optional;

public class SaveService {

    @Getter
    private static final SaveService instance = new SaveService();

    private final ProductRepository productRepository;
    private final CacheService cacheService;
    private final AuditServiceImpl auditService;

    private SaveService() {
        this.productRepository = ProductRepositoryImplPostgres.getInstance();
        this.cacheService = CacheService.getInstance();
        this.auditService = AuditServiceImpl.getInstance();
    }

    SaveService(ProductRepository productRepository,
                CacheService cacheService,
                AuditService auditService) {
        this.productRepository = productRepository;
        this.cacheService = cacheService;
        this.auditService = (AuditServiceImpl) auditService;
    }


    public void save(Product product) throws IOException {
        productRepository.save(product);
        cacheService.clearAllCaches();
    }

    public void update(Product updatedProduct) throws IOException {
        Optional<Product> oldProductOpt = cacheService.getCachedOrFromFile(updatedProduct.getId());
        if (oldProductOpt.isEmpty()) {
            throw new IllegalArgumentException("Товар с ID " + updatedProduct.getId() + " не найден.");
        }

        Product oldProduct = oldProductOpt.get();
        productRepository.save(updatedProduct);
        clearCachesAfterUpdate(oldProduct, updatedProduct);
    }

    public void delete(Product product) throws IOException {
        productRepository.delete(product);
        cacheService.clearAllCaches();
        auditService.log("Товар удалён");
    }

    private void clearCachesAfterUpdate(Product oldProduct, Product updatedProduct) {
        if (!oldProduct.getName().equals(updatedProduct.getName())) {
            cacheService.clearNameCache();
        }
        if (!oldProduct.getCategory().equals(updatedProduct.getCategory())) {
            cacheService.clearCategoryCache();
        }
        if (!oldProduct.getBrand().equals(updatedProduct.getBrand())) {
            cacheService.clearBrandCache();
        }
        if (!oldProduct.getPrice().equals(updatedProduct.getPrice())) {
            cacheService.clearPriceCache();
        }
    }

}
