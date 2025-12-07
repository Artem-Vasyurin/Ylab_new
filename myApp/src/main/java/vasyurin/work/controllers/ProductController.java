package vasyurin.work.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vasyurin.work.dto.Product;
import vasyurin.work.services.ProductService;
import vasyurin.work.services.ProductValidator;
import vasyurin.work.services.SaveService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ProductController {

    private final SaveService saveService;
    private final ProductService productService;
    private final ProductValidator validator;


    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "/product/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Product> create(@RequestBody Product product) throws IOException {

        log.info("Получен продукт: {}", product);

        saveService.save(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(product);
    }
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(value = "/product/delete", consumes = "application/json")
    public ResponseEntity<Void> delete(@RequestBody Product product) throws IOException {

        saveService.delete(product);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/product/get")
    public Object getProducts(@RequestBody Product filter) {

        List<String> validationErrors = validator.validate(filter);
        if (!validationErrors.isEmpty()) {
            return new ErrorResponse(validationErrors);
        }

        return productService.getFilteredProducts(filter);
    }

    record ErrorResponse(List<String> validationErrors) {
    }
}
