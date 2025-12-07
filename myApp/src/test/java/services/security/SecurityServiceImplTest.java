//package vasyurin.work.services.security;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import vasyurin.work.dto.User;
//import vasyurin.work.repository.UserRepository;
//
//import java.lang.reflect.Field;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@Testcontainers
//class SecurityServiceImplTest {
//
//    private SecurityServiceImpl securityService;
//    private UserRepository userRepositoryMock;
//
//    public SecurityServiceImplTest(SecurityServiceImpl securityService) {
//        this.securityService = securityService;
//
//    }
//
//    @BeforeEach
//    void setUp() throws Exception {
//
//
//        userRepositoryMock = mock(UserRepository.class);
//
//        Field field = SecurityServiceImpl.class.getDeclaredField("userRepository");
//        field.setAccessible(true);
//        field.set(securityService, userRepositoryMock);
//    }
//
//    @Test
//    void testLogin_Success() {
//        User loginRequest = new User();
//        loginRequest.setUsername("test");
//        loginRequest.setPassword("123");
//
//        User userFromDb = new User();
//        userFromDb.setUsername("test");
//        userFromDb.setPassword("123");
//
//        when(userRepositoryMock.findByUsername("test")).thenReturn(Optional.of(userFromDb));
//
//        Optional<User> result = securityService.login(loginRequest);
//
//        assertTrue(result.isPresent());
//        assertEquals("test", result.get().getUsername());
//        verify(userRepositoryMock).findByUsername("test");
//    }
//
//    @Test
//    void testLogin_WrongPassword() {
//        User loginRequest = new User();
//        loginRequest.setUsername("test");
//        loginRequest.setPassword("wrong");
//
//        User userFromDb = new User();
//        userFromDb.setUsername("test");
//        userFromDb.setPassword("123");
//
//        when(userRepositoryMock.findByUsername("test")).thenReturn(Optional.of(userFromDb));
//
//        Optional<User> result = securityService.login(loginRequest);
//
//        assertFalse(result.isPresent());
//        verify(userRepositoryMock).findByUsername("test");
//    }
//
//    @Test
//    void testLogin_UserNotFound() {
//        User loginRequest = new User();
//        loginRequest.setUsername("unknown");
//        loginRequest.setPassword("123");
//
//        when(userRepositoryMock.findByUsername("unknown")).thenReturn(Optional.empty());
//
//        Optional<User> result = securityService.login(loginRequest);
//
//        assertFalse(result.isPresent());
//        verify(userRepositoryMock).findByUsername("unknown");
//    }
//
//    @Test
//    void testLogin_NullRequest() {
//        assertFalse(securityService.login(null).isPresent());
//
//        User requestWithNullFields = new User();
//        assertFalse(securityService.login(requestWithNullFields).isPresent());
//    }
//
//    @Test
//    void testGetByToken_Success() {
//        User user1 = new User();
//        user1.setToken("token1");
//        User user2 = new User();
//        user2.setToken("token2");
//
//        when(userRepositoryMock.getAll()).thenReturn(List.of(user1, user2));
//
//        Optional<User> result = securityService.getByToken("token2");
//
//        assertTrue(result.isPresent());
//        assertEquals("token2", result.get().getToken());
//        verify(userRepositoryMock).getAll();
//    }
//
//    @Test
//    void testGetByToken_NotFound() {
//        when(userRepositoryMock.getAll()).thenReturn(List.of());
//
//        Optional<User> result = securityService.getByToken("unknown");
//
//        assertFalse(result.isPresent());
//        verify(userRepositoryMock).getAll();
//    }
//
//    @Test
//    void testGetByToken_NullToken() {
//        Optional<User> result = securityService.getByToken(null);
//        assertFalse(result.isPresent());
//    }
//}
