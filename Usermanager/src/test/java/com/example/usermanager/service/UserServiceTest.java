package com.example.usermanager.service;

import com.example.usermanager.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserRepository userRepository;

    public UserServiceTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void addUser() {
        UserService userService = new UserService();
    }

    @Test
    void alterPassword() {
    }

    @Test
    void deleteAccount() {
    }
}