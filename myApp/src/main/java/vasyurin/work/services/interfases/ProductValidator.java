package vasyurin.work.services.interfases;

import vasyurin.work.dto.Product;

import java.util.List;

public interface ProductValidator {
    List<String> validate(Product product);
}
