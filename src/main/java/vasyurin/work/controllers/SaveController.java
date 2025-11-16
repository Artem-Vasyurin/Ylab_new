package vasyurin.work.controllers;

import lombok.Getter;
import vasyurin.work.dto.Product;
import vasyurin.work.services.SaveService;

import java.io.IOException;

public class SaveController {

    @Getter
    private static final SaveController instance = new SaveController();

    private final SaveService saveService;

    private SaveController() {
        this.saveService = SaveService.getInstance();
    }

    public void save(Product product) {
        try {
            saveService.save(product);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    public void update(Product product) {
        try {
            saveService.update(product);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Ошибка при обновлении: " + e.getMessage());
        }
    }

    public void delete(Product product) {
        try {
            saveService.delete(product);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Ошибка при удалении: " + e.getMessage());
        }
    }
}
