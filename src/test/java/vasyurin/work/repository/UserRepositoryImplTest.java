package vasyurin.work.repository;

import org.junit.jupiter.api.*;
import vasyurin.work.dto.User;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    private UserRepositoryImpl repository;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        repository = UserRepositoryImpl.getInstance();

        tempFile = File.createTempFile("usersTest", ".txt");
        tempFile.deleteOnExit();

        repository.setFilePathForTest(tempFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void saveAndFindByUsername_shouldWork() throws IOException {
        User user = new User("john", "1234", "USER");

        repository.save(user);
        Optional<User> result = repository.findByUsername("john");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
        assertEquals("1234", result.get().getPassword());
        assertEquals("USER", result.get().getRole());
    }

    @Test
    void save_shouldUpdateExistingUser() throws IOException {
        User user = new User("alice", "pass1", "USER");
        repository.save(user);

        User updatedUser = new User("alice", "newpass", "USER");
        repository.save(updatedUser);

        Optional<User> result = repository.findByUsername("alice");
        assertTrue(result.isPresent());
        assertEquals("newpass", result.get().getPassword());
    }

    @Test
    void getAll_shouldReturnAllUsers() throws IOException {
        User user1 = new User("bob", "123", "ADMIN");
        User user2 = new User("carol", "456", "USER");

        repository.save(user1);
        repository.save(user2);

        List<User> allUsers = repository.getAll();
        assertEquals(2, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(u -> u.getUsername().equals("bob")));
        assertTrue(allUsers.stream().anyMatch(u -> u.getUsername().equals("carol")));
    }

    @Test
    void findByUsername_notExisting_returnsEmpty() {
        Optional<User> result = repository.findByUsername("nonexistent");
        assertTrue(result.isEmpty());
    }
}
