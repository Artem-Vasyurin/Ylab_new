package vasyurin.work.utilites;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;

@Mapper(componentModel = "default")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toDto(ProductEntity entity);

    ProductEntity toEntity(Product dto);
}
