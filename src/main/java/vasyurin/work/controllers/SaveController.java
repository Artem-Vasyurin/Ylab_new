package vasyurin.work.controllers;

import lombok.Getter;
import vasyurin.work.dto.Product;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.repository.ProductRepositoryImpl;
import java.io.IOException;
import java.util.Optional;


public class SaveController {

    @Getter
    private static SaveController instance = new SaveController();

    private final ProductRepository productRepository;
    private final CatalogController catalogController;

    private SaveController() {
        this.productRepository = ProductRepositoryImpl.getInstance();
        this.catalogController = CatalogController.getInstance();
    }

    public void save(Product product) {
        try {
            productRepository.save(product);
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    public void update(Product updatedProduct) {

        Optional<Product> oldProductOptional = catalogController.getCachedOrFromFile(updatedProduct.getId());

        if (oldProductOptional.isEmpty()) {
            System.err.println("Товар с ID " + updatedProduct.getId() + " не найден.");
            return;
        }

        Product oldProduct = oldProductOptional.get();

        try {
            productRepository.save(updatedProduct);
            clearCachesAfterUpdate(oldProduct, updatedProduct);
        } catch (IOException e) {
            System.err.println("Ошибка при обновлении: " + e.getMessage());
        }
    }

    public void delete(Product product) {
        try {
            productRepository.delete(product);
            catalogController.clearAllCaches();
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private void clearCachesAfterUpdate(Product oldProduct, Product updatedProduct) {
        if (!oldProduct.getName().equals(updatedProduct.getName())) {
            catalogController.clearNameCache();
        }
        if (!oldProduct.getCategory().equals(updatedProduct.getCategory())) {
            catalogController.clearCategoryCache();
        }
        if (!oldProduct.getBrand().equals(updatedProduct.getBrand())) {
            catalogController.clearBrandCache();
        }
        if (!oldProduct.getPrice().equals(updatedProduct.getPrice())) {
            catalogController.clearPriceCache();
        }
    }
}
