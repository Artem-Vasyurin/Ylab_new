package vasyurin.work.utility;


import lombok.Getter;
import lombok.Setter;
import vasyurin.work.controllers.AuthController;
import vasyurin.work.controllers.CatalogController;
import vasyurin.work.controllers.SaveController;
import vasyurin.work.dto.Product;
import vasyurin.work.dto.User;
import vasyurin.work.services.AuditService;
import vasyurin.work.services.MetricsService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUtility {

    @Getter
    private static final ConsoleUtility instance = new ConsoleUtility();
    public static final Scanner SC = new Scanner(System.in);

    public final CatalogController catalogController;
    public final SaveController saveController;
    public final AuditService auditService;
    public final MetricsService metricsService;

    @Setter
    @Getter
    private User currentUser;

    private ConsoleUtility() {
        this.catalogController = CatalogController.getInstance();
        this.saveController = SaveController.getInstance();
        this.auditService = AuditService.getInstance();
        this.metricsService = MetricsService.getInstance();
    }

    public void start() {
        System.out.println("Добро пожаловать в магазин!");

        while (!Thread.currentThread().isInterrupted()) {
            System.out.println();
            System.out.println("1. Просмотреть каталог");
            System.out.println("2. Добавить товар");
            System.out.println("3. Поиск");
            System.out.println("4. Войти в другой аккаунт");
            System.out.println("5. Выйти");
            System.out.println("Введите номер действия:");

            int choice = Integer.parseInt(SC.nextLine());

            switch (choice) {
                case 1:
                    viewCatalog();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    searchProduct();
                    break;
                case 4:
                    currentUser = authorizeUser();
                    if (currentUser == null) {
                        System.out.println("Авторизация не удалась. Завершение программы.");
                        auditService.log(null, "Неудачная попытка входа");
                        return;
                    }
                    auditService.log(currentUser, "Вход в систему");
                    setCurrentUser(currentUser);
                    System.out.println("Здравствуйте, " + currentUser.getUsername() + "! Ваша роль: " + currentUser.getRole());
                    break;
                case 5:
                    Thread.currentThread().interrupt();
                    break;
                default:
                    System.out.println("Неверный номер!");
            }
        }
    }

    private User authorizeUser() {
        for (int attempts = 0; attempts < 3; attempts++) {
            System.out.print("Логин: ");
            String username = SC.nextLine();
            System.out.print("Пароль: ");
            String password = SC.nextLine();

            Optional<User> userOpt = AuthController.getInstance().login(username, password);
            if (userOpt.isPresent()) {
                return userOpt.get();
            } else {
                System.out.println("Неверный логин или пароль. Попробуйте снова.");
            }
        }
        return null;
    }

    private void viewCatalog() {
        catalogController.getAll().stream()
                .map(Product::toString)
                .forEach(System.out::println);
        productSelection(catalogController.getAll());
    }

    private void searchByName() {
        System.out.print("Введите название: ");
        String name = SC.nextLine();
        metricsService.measureExecutionTime("Поиск по названию товара", () -> catalogController
                .getByName(name).stream()
                .map(Product::toString)
                .forEach(System.out::println));
        productSelection(catalogController.getByName(name));
    }

    private void searchByCategory() {
        System.out.print("Введите категорию: ");
        String category = SC.nextLine();

        catalogController.getByCategory(category).stream()
                .map(Product::toString)
                .forEach(System.out::println);
        productSelection(catalogController.getByCategory(category));
    }

    private void searchByPrice() {
        System.out.print("Введите цену: ");
        int price = Integer.parseInt(SC.nextLine());
        catalogController.getByPrice(price).stream()
                .map(Product::toString)
                .forEach(System.out::println);
        productSelection(catalogController.getByPrice(price));
    }

    private void searchByBrand() {
        System.out.print("Введите бренд: ");
        String brand = SC.nextLine();
        catalogController.getByBrand(brand).stream()
                .map(Product::toString)
                .forEach(System.out::println);
        productSelection(catalogController.getByBrand(brand));
    }

    private void productSelection(List<Product> products) {

        if (products.isEmpty()) {
            System.out.println("Ничего не найдено!");
            return;
        }
        while (true) {

            for (int i = 0; i < products.size(); i++) {
                System.out.println((i + 1) + ". " + products.get(i).getName());
            }
            System.out.println((products.size() + 1) + ". Назад");

            System.out.print("Выберите товар: ");
            int choice = Integer.parseInt(SC.nextLine());


            if (choice > 0 && choice <= products.size()) {
                productSubmenu(Optional.of(products.get(choice - 1)));
                break;
            } else if (choice == products.size() + 1) {
                return;
            }else {
                System.out.println("Неверный выбор!");
            }

        }

    }

    private void productSubmenu(Optional<Product> product) {
        while (true){

            System.out.println(product);

            System.out.println("1. Изменить товар");
            System.out.println("2. Удалить товар");
            System.out.println("3. Назад");

            int choice = Integer.parseInt(SC.nextLine());

            switch (choice) {
                case 1:
                    updateProduct(product);
                    break;
                case 2:
                    deleteProduct(product);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Неверный номер!");
            }
        }

    }

    private void updateProduct(Optional<Product> product) {
        if (!isAdmin()) return;

        while (true){
            System.out.println("1. Изменить название");
            System.out.println("2. Изменить описание");
            System.out.println("3. Изменить категорию");
            System.out.println("4. Изменить цену");
            System.out.println("5. Изменить бренд");
            System.out.println("6. Назад");

            int choice = Integer.parseInt(SC.nextLine());

            switch (choice) {
                case 1:
                    updateName(product);
                    break;
                case 2:
                    updateDescription(product);
                    break;
                case 3:
                    updateCategory(product);
                    break;
                case 4:
                    updatePrice(product);
                    break;
                case 5:
                    updateBrand(product);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Неверный номер!");
            }
        }

    }

    private void updateName(Optional<Product> product) {
        System.out.println("Введите новое название товара: ");
        product.get().setName(SC.nextLine());
        saveController.update(product.get());
        System.out.println(product);
        auditService.log(currentUser, "Изменение имени");
    }

    private void updateDescription(Optional<Product> product) {
        System.out.println("Введите новое описание товара: ");
        product.get().setDescription(SC.nextLine());
        saveController.update(product.get());
        System.out.println(product);
        auditService.log(currentUser, "Изменение описания");
    }

    private void updateCategory(Optional<Product> product) {
        System.out.println("Введите новую категорию товара: ");
        product.get().setCategory(SC.nextLine());
        saveController.update(product.get());
        System.out.println(product);
        auditService.log(currentUser, "Изменение категории");
    }

    private void updatePrice(Optional<Product> product) {
        System.out.println("Введите новую цену товара: ");
        product.get().setPrice(Integer.parseInt(SC.nextLine()));
        saveController.update(product.get());
        System.out.println(product);
        auditService.log(currentUser, "Изменение цены");
    }

    private void updateBrand(Optional<Product> product) {
        System.out.println("Введите новый бренд товара: ");
        product.get().setBrand(SC.nextLine());
        saveController.update(product.get());
        System.out.println(product);
        auditService.log(currentUser, "Изменение бренда");
    }

    private void deleteProduct(Optional<Product> product) {
        if (!isAdmin()) return;

        saveController.delete(product.get());
        auditService.log(currentUser, "Товар удалён");
    }

    private void addProduct() {

        if (!isAdmin()) return;

        Product product = new Product();

        System.out.print("Введите название: ");
        product.setName(SC.nextLine());

        System.out.print("Введите описание: ");
        product.setDescription(SC.nextLine());

        System.out.print("Введите категорию: ");
        product.setCategory(SC.nextLine());

        System.out.print("Введите цену: ");
        product.setPrice(Integer.parseInt(SC.nextLine()));

        System.out.print("Введите бренд: ");
        product.setBrand(SC.nextLine());

        metricsService.measureExecutionTime("Добавление товара", () -> saveController.save(product));
        auditService.log(currentUser, "Товар создан");
    }

    private void searchProduct() {
        System.out.println("Выберите критерий поиска: ");
        while (true){

            System.out.println("1. Поиск по названию");
            System.out.println("2. Поиск по категории");
            System.out.println("3. Поиск по цене");
            System.out.println("4. Поиск по бренду");
            System.out.println("5. Назад");

            int choice = Integer.parseInt(SC.nextLine());

            switch (choice) {
                case 1:
                    searchByName();
                    break;
                case 2:
                    searchByCategory();
                    break;
                case 3:
                    searchByPrice();
                    break;
                case 4:
                    searchByBrand();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Неверный номер!");
            }
        }
    }

    private boolean isAdmin() {
        User user = getCurrentUser();
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            System.out.println("Только администратор может это сделать.");
            return false;
        }
        return true;
    }
}
