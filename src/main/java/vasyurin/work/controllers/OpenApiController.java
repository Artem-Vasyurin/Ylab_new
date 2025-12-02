package vasyurin.work.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OpenApiController {

    private final ObjectMapper mapper;

    public OpenApiController(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping(value = "/v3/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public String apiDocs() throws Exception {

        Map<String, Object> paths = new java.util.LinkedHashMap<>();

        paths.put("/login", Map.of(
                "post", Map.of(
                        "summary", "Авторизация пользователя",
                        "requestBody", Map.of(
                                "required", true,
                                "content", Map.of(
                                        "application/json", Map.of(
                                                "schema", Map.of("type", "object")
                                        )
                                )
                        ),
                        "responses", Map.of(
                                "200", Map.of("description", "OK"),
                                "401", Map.of("description", "Unauthorized")
                        )
                )
        ));

        paths.put("/catalog", Map.of(
                "post", Map.of(
                        "summary", "Просмотр каталога по заданным полям",
                        "security", new Map[]{Map.of("X-Auth-Token", new String[]{})},
                        "requestBody", Map.of(
                                "required", true,
                                "content", Map.of(
                                        "application/json", Map.of(
                                                "schema", Map.of("type", "object")
                                        )
                                )
                        ),
                        "responses", Map.of("200", Map.of("description", "OK"))
                )
        ));

        paths.put("/product/create", Map.of(
                "post", Map.of(
                        "summary", "Создание/обновление продукта",
                        "security", new Map[]{Map.of("X-Auth-Token", new String[]{})},
                        "requestBody", Map.of(
                                "required", true,
                                "content", Map.of(
                                        "application/json", Map.of(
                                                "schema", Map.of("type", "object")
                                        )
                                )
                        ),
                        "responses", Map.of("201", Map.of("description", "Created"))
                )
        ));

        paths.put("/product/delete", Map.of(
                "delete", Map.of(
                        "summary", "Удаление продукта по gtin",
                        "security", new Map[]{Map.of("X-Auth-Token", new String[]{})},
                        "requestBody", Map.of(
                                "required", true,
                                "content", Map.of(
                                        "application/json", Map.of(
                                                "schema", Map.of("type", "object")
                                        )
                                )
                        ),
                        "responses", Map.of("204", Map.of("description", "No Content"))
                )
        ));

        Map<String, Object> openApi = Map.of(
                "openapi", "3.0.1",
                "info", Map.of(
                        "title", "Y_LAB API",
                        "version", "1.0",
                        "description", "Swagger для приложения Y_LAB"
                ),
                "paths", paths,
                "components", Map.of(
                        "securitySchemes", Map.of(
                                "X-Auth-Token", Map.of(
                                        "type", "apiKey",
                                        "in", "header",
                                        "name", "X-Auth-Token"
                                )
                        )
                )
        );

        return mapper.writeValueAsString(openApi);
    }

}
