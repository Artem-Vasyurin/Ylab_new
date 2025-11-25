package vasyurin.work.entitys;

import lombok.*;
import vasyurin.work.enams.ProductCategory;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductEntity {
    private Integer id;
    private Integer gtin;
    private String name;
    private String description;
    private ProductCategory category;
    private BigDecimal price;
    private String brand;
}

