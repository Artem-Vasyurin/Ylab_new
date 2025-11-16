package vasyurin.work.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CatalogControllerTest {

    private CatalogController catalogController;

    @BeforeEach
    void setUp() {
        catalogController = CatalogController.getInstance();
    }

    @Test
    void getAll() {
        catalogController.getAll();
    }

    @Test
    void getById() {
    }

    @Test
    void getByName() {
    }

    @Test
    void getByCategory() {
    }

    @Test
    void getByPrice() {
    }

    @Test
    void getByBrand() {
    }

    @Test
    void clearNameCache() {
    }

    @Test
    void clearCategoryCache() {
    }

    @Test
    void clearBrandCache() {
    }

    @Test
    void clearPriceCache() {
    }

    @Test
    void clearAllCaches() {
    }

    @Test
    void getCachedOrFromFile() {
    }

    @Test
    void getInstance() {
    }
}