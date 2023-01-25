package com.example.usermanager.controller;

import com.example.usermanager.model.User;
import com.example.usermanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(){
        return "logout";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/user")
    public String user(Model model) throws Exception{
        String username;
        User user;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
            user = userRepository.findByUsername(username);
            model.addAttribute("user", user);
        } else {
            throw new Exception("User not found");
        }
        return "authPages/user";
    }

    /*
    @GetMapping("/edit")
    public String edit(Model model) throws Exception{
        String username;
        User user;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
            user = userRepository.findByUsername(username);
            model.addAttribute("user", user);
        } else {
            throw new Exception("User not found");
        }
        return "authPages/edit";
    }*/

}
