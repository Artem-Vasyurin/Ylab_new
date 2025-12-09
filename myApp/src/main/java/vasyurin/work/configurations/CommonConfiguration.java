package vasyurin.work.configurations;

import auditaspect.annotationsAudit.EnableAuditAspect;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Главная конфигурация Spring MVC приложения.
 * <p>
 * Отвечает за:
 * <ul>
 *     <li>Сканирование компонентов в пакете {@code vasyurin.work}</li>
 *     <li>Подключение property source (application.yaml)</li>
 *     <li>Включение AOP через {@link EnableAuditAspect} и {@link EnableAspectJAutoProxy}</li>
 *     <li>Настройку SpringDoc</li>
 *     <li>Объявление общих бинов, таких, как {@link ObjectMapper} и {@link OpenAPI}</li>
 * </ul>
 */
@EnableAuditAspect
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "vasyurin.work")
@PropertySource(value = "classpath:application.yaml")
public class CommonConfiguration implements WebMvcConfigurer {

    /**
     * Бин ObjectMapper для JSON сериализации/десериализации.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * Настройка OpenAPI для Swagger.
     *
     * @return OpenAPI объект с информацией о проекте
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Y_LAB API")
                        .version("1.0")
                        .description("Swagger с JWT авторизацией"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ));
    }

}