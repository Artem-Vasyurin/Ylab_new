package vasyurin.work.repository;

import vasyurin.work.dto.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user) throws IOException;
    Optional<User> findByUsername(String username);
    List<User> getAll();
}