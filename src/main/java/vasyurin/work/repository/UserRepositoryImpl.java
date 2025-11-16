package vasyurin.work.repository;

import lombok.Getter;
import vasyurin.work.dto.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    @Getter
    private static final UserRepositoryImpl instance = new UserRepositoryImpl();
    private static String FILE_PATH = "users.txt";

    public void setFilePathForTest(String path) {
        FILE_PATH = path;
    }

    @Override
    public void save(User user) throws IOException {
        List<User> users = getAll();
        boolean updated = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                updated = true;
                break;
            }
        }

        if (!updated) {
            users.add(user);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User u : users) {
                writer.write(formatUser(u));
                writer.newLine();
            }
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return getAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseUser(line).ifPresent(users::add);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении пользователей: " + e.getMessage());
        }
        return users;
    }

    private String formatUser(User user) {
        return user.getUsername() + ";" + user.getPassword() + ";" + user.getRole();
    }

    private Optional<User> parseUser(String line) {
        String[] parts = line.split(";");
        if (parts.length != 3) return Optional.empty();
        return Optional.of(new User(parts[0], parts[1], parts[2]));
    }
}
