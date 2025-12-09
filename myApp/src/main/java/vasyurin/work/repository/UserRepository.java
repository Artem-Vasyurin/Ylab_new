package vasyurin.work.repository;

import vasyurin.work.dto.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для операций с пользователями в хранилище.
 * Определяет методы сохранения, поиска и получения всех пользователей.
 */
public interface UserRepository {
    /**
     * Сохраняет пользователя в хранилище.
     *
     * @param user пользователь для сохранения
     * @throws IOException если произошла ошибка ввода-вывода
     */
    void save(User user) throws IOException;

    /**
     * Ищет пользователя по имени.
     *
     * @param username имя пользователя
     * @return Optional с пользователем, если найден; пустой Optional иначе
     */
    Optional<User> findByUsername(String username);

    /**
     * Возвращает список всех пользователей.
     *
     * @return список пользователей
     */
    List<User> getAll();
}