package vasyurin.work.controllers;

import annotationsAudit.Auditing;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vasyurin.work.dto.Product;
import vasyurin.work.services.ProductServiceImpl;
import vasyurin.work.services.ProductValidatorImpl;
import vasyurin.work.services.SaveServiceImpl;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ProductController {

    private final SaveServiceImpl saveServiceImpl;
    private final ProductServiceImpl productServiceImpl;
    private final ProductValidatorImpl validator;

    @Auditing
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "/product/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Product> create(@RequestBody Product product) throws IOException {

        log.warn("Получен продукт: {}", product);

        saveServiceImpl.save(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(product);
    }

    @Auditing
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(value = "/product/delete", consumes = "application/json")
    public ResponseEntity<Void> delete(@RequestBody Product product) throws IOException {

        saveServiceImpl.delete(product);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/product/get", consumes = "application/json", produces = "application/json")
    @SecurityRequirement(name = "bearerAuth")
    @Auditing
    public List<Product> getProducts(@RequestBody Product filter) {
        List<String> validationErrors = validator.validate(filter);
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException(validationErrors.toString());
        }
        return productServiceImpl.getFilteredProducts(filter);
    }

}
