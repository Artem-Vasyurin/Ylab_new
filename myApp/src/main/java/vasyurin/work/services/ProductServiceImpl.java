package vasyurin.work.services;

import loggermetricksaspect.annotations.LoggingServices;
import org.springframework.stereotype.Service;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.services.interfases.ProductService;
import vasyurin.work.utilites.ProductMapper;

import java.util.List;

/**
 * Сервис для работы с продуктами.
 * <p>
 * Предоставляет методы получения продуктов с фильтрацией и кешированием.
 * Использует {@link ProductRepository} для работы с базой данных,
 * {@link ProductMapper} для преобразования между DTO и Entity,
 * и {@link CacheServiceImpl} для кэширования результатов.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final CacheServiceImpl cacheServiceImpl;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper mapper, CacheServiceImpl cacheServiceImpl) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.cacheServiceImpl = cacheServiceImpl;
    }

    /**
     * Возвращает список продуктов, удовлетворяющих заданному фильтру.
     * <p>
     * Сначала проверяет кэш {@link CacheServiceImpl}. Если данных нет —
     * получает продукты из базы через {@link ProductRepository},
     * преобразует их в DTO с помощью {@link ProductMapper} и сохраняет в кэш.
     *
     * @param filter объект фильтрации ({@link Product})
     * @return список продуктов, соответствующих фильтру
     */
    @LoggingServices
    @Override
    public List<Product> getFilteredProducts(Product filter) {

        List<Product> getProductFromCache = cacheServiceImpl.get(filter);

        if (getProductFromCache == null) {
            ProductEntity filterEntity = mapper.toEntity(filter);

            List<Product> listProducts = productRepository.findFilteredProducts(filterEntity)
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            cacheServiceImpl.put(filter, listProducts);
            return listProducts;
        } else {
            return getProductFromCache;
        }
    }
}
