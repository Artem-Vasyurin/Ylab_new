package vasyurin.work.services;

import annotations.LoggingServices;
import org.springframework.stereotype.Service;
import vasyurin.work.annotations.AuditAction;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.utilites.ProductMapper;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final CacheService cacheService;

    public ProductService(ProductRepository productRepository, ProductMapper mapper, CacheService cacheService) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.cacheService = cacheService;
    }

    @AuditAction
    @LoggingServices
    public List<Product> getFilteredProducts(Product filter) {

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
