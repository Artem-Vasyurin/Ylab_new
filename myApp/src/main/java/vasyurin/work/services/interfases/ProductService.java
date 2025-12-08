package vasyurin.work.services.interfases;

import vasyurin.work.dto.Product;

import java.util.List;

public interface ProductService {
    List<Product> getFilteredProducts(Product filter);
}
