package vasyurin.work.repository;

import lombok.Getter;
import vasyurin.work.dto.Product;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ProductRepositoryImpl implements ProductRepository {

    @Getter
    private static final ProductRepositoryImpl instance = new ProductRepositoryImpl();
    private static final String FILE_PATH = "products.txt";

    @Override
    public void save(Product product) throws IOException {
        List<Product> products = readAllProductsFromFile();
        boolean updated = false;

        if (product.getId() == null) {
            product.setId(getNextId());
        }

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == product.getId()) {
                products.set(i, product);
                updated = true;
                break;
            }
        }

        if (!updated) {
            products.add(product);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(new File(FILE_PATH).toPath())) {
            for (Product p : products) {
                writer.write(formatProduct(p));
                writer.newLine();
            }
        }
    }

    @Override
    public List<Product> getAll() {
        return readAllProductsFromFile();
    }

    @Override
    public Optional<Product> getById(int id) {
        return readAllProductsFromFile().stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public List<Product> getByName(String name) {
        return getAll().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .toList();
    }

    @Override
    public List<Product> getByCategory(String category) {
        return getAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    @Override
    public List<Product> getByBrand(String brand) {
        return getAll().stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(brand))
                .toList();
    }

    @Override
    public List<Product> getByPrice(int price) {
        return getAll().stream()
                .filter(p -> p.getPrice() == price)
                .toList();
    }

    @Override
    public void delete(Product product) throws IOException {
        List<Product> products = readAllProductsFromFile();
        List<String> lines = new ArrayList<>();

        for (Product p : products) {
            if (p.getId() != product.getId()) {
                lines.add(formatProduct(p));
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(new File(FILE_PATH).toPath())) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private List<Product> readAllProductsFromFile() {
        List<Product> products = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return products;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseProduct(line).ifPresent(products::add);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return products;
    }

    private Optional<Product> parseProduct(String line) {
        String[] parts = line.split(";");

        if (parts.length != 6) return Optional.empty();

        try {
            return Optional.of(new Product(
                    Integer.parseInt(parts[0]),
                    parts[1],
                    parts[2],
                    parts[3],
                    Integer.parseInt(parts[4]),
                    parts[5]
            ));
        } catch (NumberFormatException e) {
            System.err.println("Ошибка парсинга строки: " + line);
            return Optional.empty();
        }
    }

    private String formatProduct(Product p) {
        return String.format("%d;%s;%s;%s;%d;%s",
                p.getId(), p.getName(), p.getDescription(),
                p.getCategory(), p.getPrice(), p.getBrand());
    }

    private int getNextId() {
        return getAll().stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0) + 1;
    }

}
