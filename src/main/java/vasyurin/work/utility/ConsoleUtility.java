package vasyurin.work.utility;

import lombok.Getter;
import lombok.Setter;
import vasyurin.work.controllers.AuthController;
import vasyurin.work.controllers.CatalogController;
import vasyurin.work.controllers.SaveController;
import vasyurin.work.dto.Product;
import vasyurin.work.dto.User;
import vasyurin.work.services.AuditServiceImpl;
import vasyurin.work.services.MetricsService;
import vasyurin.work.services.security.SecurityService;
import vasyurin.work.services.security.SecurityServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUtility {

    @Getter
    private static final ConsoleUtility instance = new ConsoleUtility();
    public static final Scanner SC = new Scanner(System.in);

    private final CatalogController catalogController;
    private final SaveController saveController;
    private final AuditServiceImpl auditService;
    private final MetricsService metricsService;
    private final SecurityService securityService;

    @Setter
    @Getter
    private User currentUser;

    private ConsoleUtility() {
        this.catalogController = CatalogController.getInstance();
        this.saveController = SaveController.getInstance();
        this.auditService = AuditServiceImpl.getInstance();
        this.metricsService = MetricsService.getInstance();
        this.securityService = SecurityServiceImpl.getInstance();
    }

    public void start() {
        System.out.println("Добро пожаловать в магазин!");

        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("""
            
            ╔══════════════════════════════╗
            ║         ГЛАВНОЕ МЕНЮ         ║
            ╠══════════════════════════════╣
            ║ 1. Просмотреть каталог       ║
            ║ 2. Добавить товар            ║
            ║ 3. Поиск товара              ║
            ║ 4. Войти в аккаунт           ║
            ║ 5. Выйти из аккаунта         ║
            ║ 0. Выход                     ║
            ╚══════════════════════════════╝
            """);

            System.out.print("Введите номер действия: ");

            int choice = Integer.parseInt(SC.nextLine());

            switch (choice) {
                case 1 -> {
                    viewCatalog();
                    productSelection(catalogController.getAll());
                }
                case 2 -> addProduct();
                case 3 -> searchProduct();
                case 4 -> {if (authorize()) return;}
                case 5 -> logOut();
                case 0 -> Thread.currentThread().interrupt();
                default -> System.out.println("Неверный номер!");
            }
        }
    }

    private boolean authorize() {
        currentUser = authorizeUser();
        if (currentUser == null) {
            System.out.println("Авторизация не удалась. Завершение программы.");
            auditService.log(null, "Неудачная попытка входа");
            return true;
        }
        auditService.log(currentUser, "Вход в систему");
        setCurrentUser(currentUser);
        return false;
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

    private void logOut() {
        currentUser = null;
        System.out.println("Вы вышли из аккаунта");
    }
    private void viewCatalog() {
        catalogController.getAll().stream()
                .map(Product::toString)
                .forEach(System.out::println);
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

            System.out.print("""
                    ╔══════════════════════════════╗
                    ║        СПИСОК ТОВАРОВ        ║
                    ╠══════════════════════════════╣
                    """);

            for (int i = 0; i < products.size(); i++) {
                System.out.printf("║ %-1d. %-25s ║%n", i + 1, products.get(i).getName());
            }
            System.out.println("""
                    ║ 0. Назад                     ║
                    ╚══════════════════════════════╝
                    """);

            System.out.print("Выберите товар или нажмите 0, чтобы вернутся назад: ");
            int choice = Integer.parseInt(SC.nextLine());

            if (choice > 0 && choice <= products.size()) {
                productSubmenu(Optional.of(products.get(choice - 1)));
            } else if (choice == 0) {
                return;
            }else {
                System.out.println("Неверный выбор!");
            }
        }
    }

    private void productSubmenu(Optional<Product> product) {
        while (true) {
            System.out.println("""
                
                ╔══════════════════════════════╗
                ║      ДЕЙСТВИЯ С ТОВАРОМ      ║
                ╠══════════════════════════════╣
                ║ 1. Изменить товар            ║
                ║ 2. Удалить товар             ║
                ║ 0. Назад                     ║
                ╚══════════════════════════════╝
                """);

            System.out.print("Выберите действие: ");
            int choice = Integer.parseInt(SC.nextLine());

            switch (choice) {
                case 1 -> updateProduct(product);
                case 2 -> {
                    deleteProduct(product);
                    return;
                }
                case 0 -> {
                    viewCatalog();
                    return;
                }
                default -> System.out.println("Неверный номер!");
            }
        }
    }


    private void updateProduct(Optional<Product> product) {
        if (!securityService.isAdmin(currentUser)) return;

        while (true) {
            System.out.println("""
                
                ╔══════════════════════════════╗
                ║       ИЗМЕНЕНИЕ ТОВАРА       ║
                ╠══════════════════════════════╣
                ║ 1. Изменить название         ║
                ║ 2. Изменить описание         ║
                ║ 3. Изменить категорию        ║
                ║ 4. Изменить цену             ║
                ║ 5. Изменить бренд            ║
                ║ 0. Назад                     ║
                ╚══════════════════════════════╝
                """);

            System.out.print("Выберите действие: ");
            int choice = Integer.parseInt(SC.nextLine());

            switch (choice) {
                case 1 -> updateName(product);
                case 2 -> updateDescription(product);
                case 3 -> updateCategory(product);
                case 4 -> updatePrice(product);
                case 5 -> updateBrand(product);
                case 0 -> { return; }
                default -> System.out.println("Неверный номер!");
            }
        }
    }


    private void updateName(Optional<Product> product) {
        System.out.println("Введите новое название товара: ");
        product.get().setName(SC.nextLine());
        saveController.update(product.get());
        System.out.println(product);
        auditService.log(currentUser, "Изменение названия товара: " + product.get().getName());
    }

    private void updateDescription(Optional<Product> product) {
        System.out.println("Введите новое описание товара: ");
        product.get().setDescription(SC.nextLine());
        saveController.update(product.get());
        System.out.println(product);
        auditService.log(currentUser, "Изменение описания: " + product.get().getDescription());
    }

    private void updateCategory(Optional<Product> product) {
        System.out.println("Введите новую категорию товара: ");
        product.get().setCategory(SC.nextLine());
        saveController.update(product.get());
        System.out.println(product);
        auditService.log(currentUser, "Изменение категории: " + product.get().getCategory());
    }

    private void updatePrice(Optional<Product> product) {
        System.out.println("Введите новую цену товара: ");
        product.get().setPrice(Integer.parseInt(SC.nextLine()));
        saveController.update(product.get());
        System.out.println(product);
        auditService.log(currentUser, "Изменение цены: " + product.get().getPrice());
    }

    private void updateBrand(Optional<Product> product) {
        System.out.println("Введите новый бренд товара: ");
        product.get().setBrand(SC.nextLine());
        saveController.update(product.get());
        System.out.println(product);
        auditService.log(currentUser, "Изменение бренда: " + product.get().getBrand());
    }

    private void deleteProduct(Optional<Product> product) {
        if (!securityService.isAdmin(currentUser)) return;

        saveController.delete(product.get());
        auditService.log(currentUser, "Товар удалён: " + product.get().getName());
    }

    private void addProduct() {
        if (!securityService.isAdmin(currentUser)) return;

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
        while (true) {
            System.out.println("""
                
                ╔══════════════════════════════╗
                ║         ПОИСК ТОВАРОВ        ║
                ╠══════════════════════════════╣
                ║ 1. По названию               ║
                ║ 2. По категории              ║
                ║ 3. По цене                   ║
                ║ 4. По бренду                 ║
                ║ 0. Назад                     ║
                ╚══════════════════════════════╝
                """);

            System.out.print("Введите номер: ");
            int choice = Integer.parseInt(SC.nextLine());

            switch (choice) {
                case 1 -> searchByName();
                case 2 -> searchByCategory();
                case 3 -> searchByPrice();
                case 4 -> searchByBrand();
                case 0 -> { return; }
                default -> System.out.println("Неверный номер!");
            }
        }
    }
}
