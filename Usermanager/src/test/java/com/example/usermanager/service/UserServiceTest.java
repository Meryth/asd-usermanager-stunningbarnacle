package com.example.usermanager.service;

import com.example.usermanager.Exceptions.User.DBInputException;
import com.example.usermanager.model.User;
import com.example.usermanager.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class   UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService = new UserService(userRepository);
    private static BCryptPasswordEncoder bCryptPasswordEncoder;
    private static User newUser;
    private String newPassword = "test123";

    @BeforeAll
    static void setup() {
        Logger.getLogger(UserServiceTest.class.getName()).log(Level.INFO, "Executed init method.");
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        newUser = new User("Shabbir", "Islam", "shabbir123", "password");
    }

    @Test
    @DisplayName("Test to add new user")
    @Order(1)
    void addUser_username_notInUse() throws DBInputException {
        //Arrange/Act
        userService.addUser(newUser);
        User addedUser = userRepository.findByUsername(newUser.getUsername());

        //Assert
        assertEquals(addedUser.getUsername(), newUser.getUsername());
    }

    @Test
    @DisplayName("Try to add a existing user")
    @Order(2)
    void addUser_username_inUse() {
        //Arrange/Act/Assert
        assertThrows(DBInputException.class, () -> userService.addUser(newUser));
    }

    @ParameterizedTest
    @DisplayName("Change password with wrong id")
    @Order(3)
    @ValueSource(ints = {10, 0, -3, -5, Integer.MAX_VALUE})
    void alter_password_wrong_id(int id) throws DBInputException {
        //Arrange/Act/Assert
        assertThrows(DBInputException.class, () -> userService.alterPassword(id, "password", newPassword));
    }

    @Test
    @DisplayName("Change password with wrong password")
    @Order(4)
    void alter_password_with_wrong_password() throws DBInputException {
        //Arrange/Act
        User user = userRepository.findByUsername(newUser.getUsername());

        //Assert
        assertThrows(DBInputException.class, () -> userService.alterPassword(user.getId(), "password123", newPassword));
    }

    @Test
    @DisplayName("Change password")
    @Order(5)
    void alter_password() throws DBInputException {
        //Arrange
        User user = userRepository.findByUsername(newUser.getUsername());

        //Act
        userService.alterPassword(user.getId(), "password", newPassword);
        User alteredPasswordUser = userRepository.findByUsername(newUser.getUsername());

        //Assert
        assertEquals(true, bCryptPasswordEncoder.matches(newPassword, alteredPasswordUser.getPassword()));
    }

    @Test
    @DisplayName("Delete user with wrong password")
    @Order(6)
    void delete_account_with_wrong_password() throws DBInputException {
        //Arrange/Act
        User user = userRepository.findByUsername(newUser.getUsername());
        String currentPassword = "wrongPwd";

        //Assert
        assertThrows(DBInputException.class, () -> userService.deleteAccount(user.getId(), currentPassword));
    }

    @Test
    @DisplayName("Delete user")
    @Order(7)
    void delete_account() throws DBInputException {
        //Arrange
        User user = userRepository.findByUsername(newUser.getUsername());
        String currentPassword = newPassword;

        //Act
        userService.deleteAccount(user.getId(), currentPassword);

        //Assert
        assertNull(userRepository.findByUsername(newUser.getUsername()));
    }

    @AfterAll
    static void tearDown() {
        Logger.getLogger(UserServiceTest.class.getName()).log(Level.INFO, "End testing");
    }
}