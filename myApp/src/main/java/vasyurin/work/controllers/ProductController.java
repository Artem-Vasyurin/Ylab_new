package vasyurin.work.controllers;

import auditaspect.annotationsAudit.Auditing;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vasyurin.work.dto.Product;
import vasyurin.work.services.interfases.ProductService;
import vasyurin.work.services.interfases.ProductValidator;
import vasyurin.work.services.interfases.SaveService;

import java.io.IOException;
import java.util.List;


/**
 * REST-контроллер для операций над продуктами:
 * создание, удаление и получение списка по фильтру.
 */
@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ProductController {

    private final SaveService saveServiceImpl;
    private final ProductService productServiceImpl;
    private final ProductValidator validator;

    /**
     * Создаёт новый продукт или обновляет существующий,
     * если продукт с таким идентификатором уже есть.
     *
     * @param product продукт для создания или обновления
     * @return сохранённый продукт со статусом 201
     * @throws IOException если произошла ошибка записи
     */
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

    /**
     * Удаляет указанный продукт.
     *
     * @param product продукт, который нужно удалить
     * @return статус 204 (контента нет)
     * @throws IOException если произошла ошибка удаления
     */
    @Auditing
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(value = "/product/delete", consumes = "application/json")
    public ResponseEntity<Void> delete(@RequestBody Product product) throws IOException {

        saveServiceImpl.delete(product);

        return ResponseEntity.noContent().build();
    }

    /**
     * Возвращает список продуктов, соответствующих фильтру.
     *
     * @param filter объект фильтрации (например, категория, цена и т.д.)
     * @return список продуктов, удовлетворяющих фильтру
     * @throws IllegalArgumentException если фильтр некорректен
     */
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