package vasyurin.work.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vasyurin.work.utilites.YamlPropertySourceFactory;

/**
 * Главная конфигурация приложения для чистого Spring MVC.
 * <p>
 * Объединяет:
 * <ul>
 *     <li>Сканирование компонентов</li>
 *     <li>Подключение property source (application.yaml)</li>
 *     <li>Включение AOP</li>
 *     <li>Swagger UI и статический HTML</li>
 *     <li>Общие бины, такие как ObjectMapper и OpenAPI</li>
 * </ul>
 */
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "vasyurin.work")
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class CommonConfiguration implements WebMvcConfigurer {

    /**
     * Настройка Swagger UI через WebJar и статический HTML.
     *
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/4.15.5/");

        registry.addResourceHandler("/swagger.html")
                .addResourceLocations("classpath:/static/swagger.html");
    }

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
                        .description("Swagger без Spring Boot (очень тяжело)"));
    }
}
