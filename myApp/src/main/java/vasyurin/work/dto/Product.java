package vasyurin.work.dto;

import lombok.*;
import vasyurin.work.enams.ProductCategory;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    private Integer gtin;
    private String name;
    private String description;
    private ProductCategory category;
    private BigDecimal price;
    private String brand;


    @Override
    public String toString() {
        return String.format(
                "Артикул: %s, Название: %s, Описание: %s, Категория: %s, Цена: %s ₽, Бренд: %s",
                gtin, name, description, category, price != null ? price.toPlainString() : "null", brand
        );
    }

}
