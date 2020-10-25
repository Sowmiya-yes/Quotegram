package com.myApp.service;

import com.myApp.config.CustomePasswordEncoder;
import com.myApp.domain.Quotegrammer;
import com.myApp.domain.User;
//import com.myApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

//    @Autowired
//    UserRepository userRepository;
    @Autowired
    CustomePasswordEncoder passwordEncoder;

    List<User> getUsers() {
        User user1 = new User(UUID.randomUUID().toString(), "Sowmiya",
                "SOwemiya", "",
                "sowmiya@mail.com", passwordEncoder.getPasswordEncoder().encode("password"),
                new Timestamp(new Date().getTime()),
                new Timestamp(new Date().getTime()),
                true, "USER,ADMIN");
        List<User> users = new ArrayList<>();
        users.add(user1);
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        Optional<User> user = userRepository.findByUsername(userName);
//        Quotegrammer quotegrammer = userRepository.findByUsername(userName)
//                .map(Quotegrammer::new)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
//        return userRepository.findByUsername(userName)
//                .map(Quotegrammer::new)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        Quotegrammer quotegrammer = getUsers().stream()
                .filter(user -> user.getUsername().equals(userName))
                .findFirst().map(Quotegrammer::new)
                .orElse(null);
        return getUsers().stream()
                .filter(user -> user.getUsername().equals(userName))
                .findFirst().map(Quotegrammer::new)
                .orElse(null);
    }
}
