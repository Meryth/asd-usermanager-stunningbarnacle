package com.example.usermanager.service;

import com.example.usermanager.Exceptions.User.DBInputException;
import com.example.usermanager.model.User;
import com.example.usermanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    /*
     * All the logic for the application is here
     * We have:
     *   Create account
     *   Login
     *   Logout
     *   Change account information
     *   Delete account
     * */

    public UserService() {

    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) throws DBInputException {

        /*User user1 = userRepository.findByUsername(user.getUsername());
        if (user1 != null) {
            throw new DBInputException("Der Benutzername ist bereits vergeben!", null);
        }*/

        checkExistingUser(user);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new DBInputException("Es gab ein Problem beim Speichern!", null);
        }
    }

    public void checkExistingUser(User thisUser)throws DBInputException{
        if (userRepository.findByUsername(thisUser.getUsername()) != null) {
            throw new DBInputException("Der Benutzername ist bereits vergeben!", null);
        }
    }

    public void alterPassword(int id, String currentPassword, String newPassword) throws DBInputException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User NewUser = userRepository.findByID(id);

        if (NewUser == null) {
            throw new DBInputException(id + " " + bCryptPasswordEncoder.encode(currentPassword) + " " + newPassword, null);
        }

        if (!bCryptPasswordEncoder.matches(currentPassword, NewUser.getPassword())) {
            throw new DBInputException("Passwort ist nicht korrekt!", null);
        }

        NewUser.setPassword(bCryptPasswordEncoder.encode(newPassword));

        userRepository.save(NewUser);
    }

    public void deleteAccount(int id, String currentPassword) throws DBInputException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = userRepository.findByID(id);

        if (user == null) {
            throw new DBInputException("Passwort stimmt nicht!", null);
        }

        if (!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            throw new DBInputException("Passwort ist nicht korrekt", null);
        }

        userRepository.delete(user);
    }
}
