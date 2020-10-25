package com.myApp.controller;

import com.myApp.domain.HighLevelUserView;
import com.myApp.domain.Response;
import com.myApp.domain.SignupRequest;
import com.myApp.domain.User;
import com.myApp.exception.UserRelatedException;
import com.myApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/signup")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> createNewUser(@Valid @RequestBody SignupRequest signupRequest) {
        User user = User.builder()
                .userFirstName(signupRequest.getFirstName())
                .userLastName(signupRequest.getLastName())
                .username(signupRequest.getUserName())
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword())
                .build();

        try {
            userService.createNewUser(user);
        } catch (UserRelatedException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity
                .created(URI.create(String.format("/users/%s", user.getUsername())))
                .body(new Response(true, "User created"));
    }

    @GetMapping("/users")
    List<HighLevelUserView> getAllUsers() {
        return userService.getAllUsersHighLevelView();
    }

    @GetMapping("/users/{userName}")
    ResponseEntity<?> getUserByUserName(@PathVariable String userName) {
        try {
            return ResponseEntity.ok(userService.getUserByUsername(userName));
        } catch (UserRelatedException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
//
//    @GetMapping("/user/{userId}")
//    User getUserByUserId(@PathVariable String userId) {
//        return userRepository.findById(userId)
//                .orElse(null);
//    }
//
//    @PostMapping("/users")
//    User createNewUser(@RequestBody User newUser) {
//        System.out.println(newUser);
//        return userRepository.save(newUser);
//    }
//
//    @DeleteMapping("/users/{userId}")
//    boolean deleteUserByUserId(@PathVariable String userId) {
//        userRepository.deleteById(userId);
//        return true;
//    }
}
