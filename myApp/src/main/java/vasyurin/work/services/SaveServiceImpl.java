package vasyurin.work.services;

import loggermetricksaspect.annotations.LoggingServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.services.interfases.CacheService;
import vasyurin.work.services.interfases.SaveService;
import vasyurin.work.utilites.ProductMapper;

import java.io.IOException;

/**
 * Сервис для сохранения, обновления и удаления продуктов.
 * <p>
 * Использует {@link ProductRepository} для работы с базой данных,
 * {@link ProductMapper} для преобразования между DTO и Entity,
 * и {@link CacheServiceImpl} для управления кэшем.
 * <p>
 * После каждой операции кэш очищается.
 */
@Slf4j
@Service
@AllArgsConstructor
public class SaveServiceImpl implements SaveService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final CacheService cacheServiceImpl;

    /**
     * Сохраняет новый продукт или обновляет существующий в базе данных.
     * <p>
     * Преобразует DTO в Entity с помощью {@link ProductMapper}, сохраняет через {@link ProductRepository}
     * и очищает кэш {@link CacheServiceImpl}.
     *
     * @param dto продукт для сохранения
     * @throws IOException при ошибках сохранения
     */
    @LoggingServices
    @Override
    public void save(Product dto) throws IOException {
        ProductEntity entity = mapper.toEntity(dto);
        productRepository.save(entity);
        cacheServiceImpl.clear();
    }

    /**
     * Обновляет существующий продукт в базе данных.
     * <p>
     * Требует, чтобы GTIN продукта был задан (не null).
     * Преобразует DTO в Entity, сохраняет через {@link ProductRepository} и очищает кэш.
     *
     * @param dto продукт для обновления
     * @throws IOException              при ошибках сохранения
     * @throws IllegalArgumentException если GTIN равен null
     */
    @LoggingServices
    @Override
    public void update(Product dto) throws IOException {
        if (dto.getGtin() == null) {
            throw new IllegalArgumentException("GTIN не может быть null при обновлении");
        }

        ProductEntity newEntity = mapper.toEntity(dto);
        productRepository.save(newEntity);
        cacheServiceImpl.clear();
    }

    /**
     * Удаляет продукт из базы данных по GTIN.
     * <p>
     * После удаления очищает кэш {@link CacheServiceImpl}.
     *
     * @param product продукт для удаления
     * @throws IOException при ошибках удаления
     */
    @LoggingServices
    @Override
    public void delete(Product product) throws IOException {
        productRepository.delete(product.getGtin());
        cacheServiceImpl.clear();
    }
}