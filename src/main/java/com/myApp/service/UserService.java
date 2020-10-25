package com.myApp.service;

import com.myApp.config.CustomePasswordEncoder;
import com.myApp.domain.HighLevelUserView;
import com.myApp.domain.User;
import com.myApp.exception.UserRelatedException;
//import com.myApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

//    @Autowired
//    UserRepository userRepository;
    @Autowired
    CustomePasswordEncoder passwordEncoder;

    public void createNewUser(User newUser) {
        if(findByUsername(newUser.getUsername()).isPresent()) {
            throw new UserRelatedException(
                    String.format("User already found with the username %s", newUser.getUsername()));
//        } else if(findByEmail(newUser.getEmail()).isPresent()) {
//            throw new UserAlreadyFoundException(
//                    String.format("User already found with the email %s", newUser.getEmail()));
        } else {
            newUser.setActive(true);
            newUser.setRoles("USER");
            newUser.setPassword(passwordEncoder.getPasswordEncoder().encode(newUser.getPassword()));
//            userRepository.save(newUser);
            System.out.println(newUser);
        }
    }

    private Optional<User> findByUsername(String username) {
//        return Optional.of(new User(UUID.randomUUID().toString(), "Sowmiya",
//                "SOwemiya", "",
//                "sowmiya@mail.com", passwordEncoder.getPasswordEncoder().encode("password"),
//                new Timestamp(new Date().getTime()),
//                new Timestamp(new Date().getTime()),
//                true, "USER,ADMIN"));
        return Optional.empty();
    }

    List<User> allusers() {
        List<User> users = new ArrayList<>();

        users.add(new User(UUID.randomUUID().toString(), "Sowmiya",
                "SOwemiya", "",
                "sowmiya@mail.com", passwordEncoder.getPasswordEncoder().encode("password"),
                new Timestamp(new Date().getTime()),
                new Timestamp(new Date().getTime()),
                true, "USER,ADMIN"));
        users.add(new User(UUID.randomUUID().toString(), "Sowmya",
                "SOwemiya", "",
                "sowmiya@mail.com", passwordEncoder.getPasswordEncoder().encode("password"),
                new Timestamp(new Date().getTime()),
                new Timestamp(new Date().getTime()),
                true, "USER,ADMIN"));

        return users;
    }

    public List<HighLevelUserView> getAllUsersHighLevelView() {
//        return userRepository.findAll()
//                .stream()
//                .map(user -> new HighLevelUserView(user.getUsername(), user.isActive()))
//                .collect(Collectors.toList());
        return allusers()
                .stream()
                .map(user -> new HighLevelUserView(user.getUsername(), user.isActive()))
                .collect(Collectors.toList());
    }

    public User getUserByUsername(String userName) {
//        return userRepository.findByUsername(userName)
//                .orElseThrow(() -> new UserRelatedException(String.format("User %s not found", userName)));
        return findByUsername(userName)
                .orElseThrow(() -> new UserRelatedException(String.format("User %s not found", userName)));
    }
}
