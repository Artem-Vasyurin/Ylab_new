package vasyurin.work.services;

import lombok.Getter;
import vasyurin.work.dto.Product;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.repository.ProductRepositoryImpl;
import vasyurin.work.repository.ProductRepositoryImplPostgres;

import java.util.*;
import java.util.stream.Stream;

public class CacheService {

    @Getter
    private static final CacheService instance = new CacheService();

    private final ProductRepository productRepository;

    private final Map<String, List<Product>> cacheByName = new HashMap<>();
    private final Map<String, List<Product>> cacheByCategory = new HashMap<>();
    private final Map<String, List<Product>> cacheByBrand = new HashMap<>();
    private final Map<Integer, List<Product>> cacheByPrice = new HashMap<>();

    private CacheService() {
        this.productRepository = ProductRepositoryImplPostgres.getInstance();
    }

    public List<Product> getByName(String name) {
        return cacheByName.computeIfAbsent(
                name.toLowerCase(),
                k -> productRepository.getByName(name)
        );
    }

    public List<Product> getByCategory(String category) {
        return cacheByCategory.computeIfAbsent(
                category.toLowerCase(),
                k -> productRepository.getByCategory(category)
        );
    }

    public List<Product> getByBrand(String brand) {
        return cacheByBrand.computeIfAbsent(
                brand.toLowerCase(),
                k -> productRepository.getByBrand(brand)
        );
    }

    public List<Product> getByPrice(int price) {
        return cacheByPrice.computeIfAbsent(
                price,
                k -> productRepository.getByPrice(price)
        );
    }

    public Optional<Product> getCachedOrFromFile(int id) {
        return Stream.of(cacheByName, cacheByCategory, cacheByBrand)
                .flatMap(map -> map.values().stream().flatMap(List::stream))
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst()
                .or(() -> productRepository.getById(id));
    }

    public void clearNameCache() {
        cacheByName.clear();
    }

    public void clearCategoryCache() {
        cacheByCategory.clear();
    }

    public void clearBrandCache() {
        cacheByBrand.clear();
    }

    public void clearPriceCache() {
        cacheByPrice.clear();
    }

    public void clearAllCaches() {
        clearNameCache();
        clearCategoryCache();
        clearBrandCache();
        clearPriceCache();
    }
}
