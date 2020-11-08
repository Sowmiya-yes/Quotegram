package com.myApp.service;

import com.myApp.config.CustomePasswordEncoder;
import com.myApp.domain.User;
import com.myApp.exception.UserRelatedException;
import com.myApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomePasswordEncoder passwordEncoder;

//    Tested
    public void createNewUser(User newUser) {
        if(userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            throw new UserRelatedException(
                    String.format("User already found with the username %s", newUser.getUsername()));
        } else if(userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new UserRelatedException(
                    String.format("User already found with the email %s", newUser.getEmail()));
        } else {
            newUser.setActive(true);
            newUser.setRoles("USER");
            newUser.setPassword(passwordEncoder.getPasswordEncoder().encode(newUser.getPassword()));
            userRepository.save(newUser);
            System.out.println(newUser);
        }
    }

//    TESTED
    public List<User> getAllUsersHighLevelView() {
        return userRepository.findByIsActive();
    }

//    TESTED
    public User getUserByUsername(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserRelatedException(String.format("User %s not found", userName)));
    }

//    TESTED
    public Long deleteUser(String userName) {
        return userRepository.deleteByUsername(userName);
    }

//    TESTED
    public void deactivateOrReactivateUser(String userName) {
        User user = getUserByUsername(userName);
        if(user != null) {
            user.setActive(!user.isActive());
            userRepository.save(user);
;        } else {
            throw new UserRelatedException(String.format("User %s not found", userName));
        }
    }
}
