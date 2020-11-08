package com.myApp.controller;

import com.myApp.config.JwtUtil;
import com.myApp.domain.*;
import com.myApp.exception.UserRelatedException;
import com.myApp.repository.RedisRepository;
import com.myApp.repository.RedisRepositoryImpl;
import com.myApp.service.CustomUserDetailsService;
import com.myApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RedisRepository redisRepository;

//    Tested
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@Valid @RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

//    Tested
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

//     TESTED
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users")
    List<User> getAllUsers() {
        return userService.getAllUsersHighLevelView();
    }

//    TESTED
    @PreAuthorize(("hasRole('USER')"))
    @GetMapping("/user/{userName}")
    ResponseEntity<?> getUserByUserName(@PathVariable String userName) {
        try {
            return ResponseEntity.ok(userService.getUserByUsername(userName));
        } catch (UserRelatedException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

//    TESTED
    @PreAuthorize(("hasRole('USER')"))
    @DeleteMapping("/user/{userName}")
    ResponseEntity<?> deleteUserByUserId(@PathVariable String userName) {
        try {
            if(userService.deleteUser(userName) > 0)
                return new ResponseEntity<Response>(
                        new Response(false, String.format("User %s deleted", userName)), HttpStatus.NO_CONTENT);
            else
                return new ResponseEntity<Response>(
                        new Response(false, String.format("Not able to find user %s", userName)), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<Response>(
                    new Response(false, String.format("Not able to delete user %s %s", userName, e.getLocalizedMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    TESTED
    @PreAuthorize(("hasRole('USER')"))
    @PutMapping("/user/{userName}")
    ResponseEntity<?> deactivateOrReactivateUser(@PathVariable String userName) {
        try {
            userService.deactivateOrReactivateUser(userName);
            return new ResponseEntity<Response>(
                    new Response(false, String.format("User %s Deactivated or Reactivated", userName)), HttpStatus.NO_CONTENT);
        } catch (UserRelatedException e) {
            return new ResponseEntity<Response>(
                    new Response(false, String.format("Not able to find user %s", userName)), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<Response>(
                    new Response(false, String.format("Not able to deactivate user %s %s", userName, e.getLocalizedMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/redis")
    void redis(@RequestBody NewsFeed newsFeed) {
        redisRepository.save(newsFeed);
    }
}
