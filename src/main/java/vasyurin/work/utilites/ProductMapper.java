package vasyurin.work.utilites;

import org.mapstruct.Mapper;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toDto(ProductEntity entity);

    ProductEntity toEntity(Product dto);
}
