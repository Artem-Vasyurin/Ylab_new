package vasyurin.work.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vasyurin.work.dto.Product;
import vasyurin.work.services.SaveService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final SaveService saveService;

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Product> create(@RequestBody Product product) throws IOException {

        log.info("Получен продукт: {}", product);

        saveService.save(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(product);
    }

    @DeleteMapping(value = "/delete", consumes = "application/json")
    public ResponseEntity<Void> delete(@RequestBody Product product) throws IOException {

        saveService.delete(product);

        return ResponseEntity.noContent().build();
    }
}
