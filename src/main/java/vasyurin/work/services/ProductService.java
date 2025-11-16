package vasyurin.work.services;

import lombok.Getter;
import vasyurin.work.dto.Product;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.repository.ProductRepositoryImpl;
import vasyurin.work.repository.ProductRepositoryImplPostgres;

import java.util.List;
import java.util.Optional;

public class ProductService {

    @Getter
    private static final ProductService instance = new ProductService();

    private final ProductRepository productRepository;
    private final CacheService cacheService;

    private ProductService() {
        this.productRepository = ProductRepositoryImplPostgres.getInstance();
        this.cacheService = CacheService.getInstance();
    }

    public List<Product> getAll() {
        return productRepository.getAll();
    }

    public Optional<Product> getById(int id) {
        return productRepository.getById(id);
    }

    public List<Product> getByName(String name) {
        return cacheService.getByName(name);
    }

    public List<Product> getByCategory(String category) {
        return cacheService.getByCategory(category);
    }

    public List<Product> getByBrand(String brand) {
        return cacheService.getByBrand(brand);
    }

    public List<Product> getByPrice(int price) {
        return cacheService.getByPrice(price);
    }
}
