package vasyurin.work.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    private Integer id;
    private String name;
    private String description;
    private String category;
    private Integer price;
    private String brand;

    @Override
    public String toString() {
        return String.format(
                """
                ----------------------------
                ID: %d
                Название: %s
                Описание: %s
                Категория: %s
                Цена: %d ₽
                Бренд: %s
                """,
                id, name, description, category, price, brand
        );
    }
}
