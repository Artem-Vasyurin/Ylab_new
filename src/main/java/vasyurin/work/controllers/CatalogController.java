package vasyurin.work.controllers;

import lombok.Getter;
import vasyurin.work.dto.Product;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.repository.ProductRepositoryImpl;
import java.util.*;

public class CatalogController {
    @Getter
    private static final CatalogController instance = new CatalogController();
    private final ProductRepository productRepository;
    private final Map<String, List<Product>> cacheByName;
    private final Map<String, List<Product>> cacheByCategory;
    private final Map<String, List<Product>> cacheByBrand;
    private final Map<Integer, List<Product>> cacheByPrice;

    private CatalogController() {

        this.productRepository = ProductRepositoryImpl.getInstance();
        this.cacheByName = new HashMap<>();
        this.cacheByCategory = new HashMap<>();
        this.cacheByBrand = new HashMap<>();
        this.cacheByPrice = new HashMap<>();

    }

    public List<Product> getAll() {
        return productRepository.getAll();
    }

    public Optional<Product> getById(int id) {
        return  productRepository.getById(id);
    }

    public List<Product> getByName(String name) {
        return cacheByName.computeIfAbsent(name.toLowerCase(),
                k -> productRepository.getByName(name));
    }

    public List<Product> getByCategory(String category) {
        return cacheByCategory.computeIfAbsent(category.toLowerCase(),
                k -> productRepository.getByCategory(category));
    }

    public List<Product> getByPrice(int price) {
        return cacheByPrice.computeIfAbsent(price,
                k -> productRepository.getByPrice(price));
    }

    public List<Product> getByBrand(String brand) {
        return cacheByBrand.computeIfAbsent(brand.toLowerCase(),
                k -> productRepository.getByBrand(brand));
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

    public Optional<Product> getCachedOrFromFile(int id) {
        for (List<Product> list : cacheByName.values()) {
            for (Product p : list) {
                if (p.getId() == id) return Optional.of(p);
            }
        }
        for (List<Product> list : cacheByCategory.values()) {
            for (Product p : list) {
                if (p.getId() == id) return Optional.of(p);
            }
        }
        for (List<Product> list : cacheByBrand.values()) {
            for (Product p : list) {
                if (p.getId() == id) return Optional.of(p);
            }
        }
        for (List<Product> list : cacheByPrice.values()) {
            for (Product p : list) {
                if (p.getId() == id) return Optional.of(p);
            }
        }

        return productRepository.getById(id);
    }

}
