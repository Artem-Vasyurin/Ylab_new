package vasyurin.work.services;

import lombok.Getter;
import vasyurin.work.annotations.AuditAction;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.repository.ProductRepositoryImplPostgres;
import vasyurin.work.utilites.ProductMapper;

import java.util.List;

public class ProductService {

    @Getter
    private static final ProductService instance = new ProductService();

    private final ProductRepository productRepository;
    private final ProductMapper mapper = ProductMapper.INSTANCE;
    private final CacheService cacheService = CacheService.getInstance();

    private ProductService() {
        this.productRepository = ProductRepositoryImplPostgres.getInstance();
    }

    @AuditAction
    public List<Product> getFilteredProducts(Product filter) {
        System.out.println("ProductService LOADED BY: " + this.getClass().getClassLoader());

        if (cacheService.get(filter) == null) {
            ProductEntity filterEntity = mapper.toEntity(filter);

            List<Product> listProducts = productRepository.findFilteredProducts(filterEntity)
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            cacheService.put(filter, listProducts);
            return listProducts;
        } else {
            return cacheService.get(filter);
        }
    }
}
