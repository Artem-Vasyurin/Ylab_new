package vasyurin.work.controllers;

import lombok.Getter;
import vasyurin.work.dto.Product;
import vasyurin.work.services.ProductService;

import java.util.List;
import java.util.Optional;

public class CatalogController {

    @Getter
    private static final CatalogController instance = new CatalogController();

    private final ProductService productService;

    private CatalogController() {
        this.productService = ProductService.getInstance();
    }

    public List<Product> getAll() {
        return productService.getAll();
    }

    public Optional<Product> getById(int id) {
        return productService.getById(id);
    }

    public List<Product> getByName(String name) {
        return productService.getByName(name);
    }

    public List<Product> getByCategory(String category) {
        return productService.getByCategory(category);
    }

    public List<Product> getByPrice(int price) {
        return productService.getByPrice(price);
    }

    public List<Product> getByBrand(String brand) {
        return productService.getByBrand(brand);
    }

}
