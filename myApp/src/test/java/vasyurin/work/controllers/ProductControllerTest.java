package vasyurin.work.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import vasyurin.work.dto.Product;
import vasyurin.work.services.ProductServiceImpl;
import vasyurin.work.services.ProductValidatorImpl;
import vasyurin.work.services.SaveServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductController.class)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SaveServiceImpl saveServiceImpl;

    @MockBean
    private ProductServiceImpl productServiceImpl;

    @MockBean
    private ProductValidatorImpl validator;

    @Test
    @DisplayName("Создание продукта должно возвращать статус 201 и тело продукта")
    void createProduct_success() throws Exception {
        Product product = Instancio.create(Product.class);

        mockMvc.perform(post("/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(product)));

        Mockito.verify(saveServiceImpl).save(any(Product.class));
    }

    @Test
    @DisplayName("Удаление продукта должно возвращать статус 204")
    void deleteProduct_success() throws Exception {
        Product product = Instancio.create(Product.class);

        mockMvc.perform(delete("/product/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNoContent());

        Mockito.verify(saveServiceImpl).delete(any(Product.class));
    }

    @Test
    @DisplayName("Получение продуктов с валидным фильтром")
    void getProducts_success() throws Exception {
        Product filter = Instancio.create(Product.class);
        Product product = Instancio.create(Product.class);

        Mockito.when(validator.validate(any(Product.class)))
                .thenReturn(Collections.emptyList());

        Mockito.when(productServiceImpl.getFilteredProducts(any(Product.class)))
                .thenReturn(List.of(product));

        mockMvc.perform(post("/product/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(product))));
    }

    @Test
    @DisplayName("Получение продуктов с неверным фильтром выбрасывает исключение")
    void getProducts_validationError() throws Exception {
        Product filter = Instancio.create(Product.class);

        Mockito.when(validator.validate(any(Product.class)))
                .thenReturn(List.of("Поле name не может быть пустым"));

        mockMvc.perform(post("/product/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isBadRequest());
    }
}
