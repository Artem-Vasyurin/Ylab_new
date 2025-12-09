package vasyurin.work.utilites;

import org.mapstruct.Mapper;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;

/**
 * Интерфейс маппера для преобразования между {@link ProductEntity} и {@link Product}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {
    /**
     * Преобразует сущность продукта в DTO.
     *
     * @param entity объект сущности продукта
     * @return объект DTO продукта
     */
    Product toDto(ProductEntity entity);

    /**
     * Преобразует DTO продукта в сущность.
     *
     * @param dto объект DTO продукта
     * @return объект сущности продукта
     */
    ProductEntity toEntity(Product dto);
}
