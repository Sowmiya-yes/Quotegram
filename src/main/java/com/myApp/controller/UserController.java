package com.myApp.controller;

import com.myApp.domain.User;
import com.myApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user")
    User getUserByUserName(@RequestParam String userName) {
        return userRepository.findByUserName(userName);
    }

    @GetMapping("/user/{userId}")
    User getUserByUserId(@PathVariable String userId) {
        return userRepository.findById(userId)
                .orElse(null);
    }

    @PostMapping("/users")
    User createNewUser(@RequestBody User newUser) {
        System.out.println(newUser);
        newUser.setFollowerIds(Collections.emptyList());
        newUser.setUserPostIds(Collections.emptyList());
        return userRepository.save(newUser);
    }

    @DeleteMapping("/users/{userId}")
    boolean deleteUserByUserId(@PathVariable String userId) {
        userRepository.deleteById(userId);
        return true;
    }
}
