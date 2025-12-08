package vasyurin.work.services.interfases;

import vasyurin.work.dto.Product;

import java.io.IOException;

public interface SaveService {
    void save(Product dto) throws IOException;

    void update(Product dto) throws IOException;

    void delete(Product product) throws IOException;
}
