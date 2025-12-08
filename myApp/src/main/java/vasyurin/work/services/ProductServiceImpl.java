package vasyurin.work.services;

import annotations.LoggingServices;
import org.springframework.stereotype.Service;
import vasyurin.work.dto.Product;
import vasyurin.work.entitys.ProductEntity;
import vasyurin.work.repository.ProductRepository;
import vasyurin.work.services.interfases.ProductService;
import vasyurin.work.utilites.ProductMapper;

import java.util.List;

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
