package vasyurin.work.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vasyurin.work.dto.Product;
import vasyurin.work.services.ProductService;
import vasyurin.work.services.ProductValidator;

import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;
    private final ProductValidator validator;

    @GetMapping
    public String getCatalog() {
        return "Catalog is working!";
    }

    @PostMapping
    public Object filter(@RequestBody Product filter) {

        List<String> validationErrors = validator.validate(filter);
        if (!validationErrors.isEmpty()) {
            return new ErrorResponse(validationErrors);
        }

        return productService.getFilteredProducts(filter);
    }

    record ErrorResponse(List<String> validationErrors) {
    }
}
