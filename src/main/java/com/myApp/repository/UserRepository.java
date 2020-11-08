package com.myApp.repository;

import com.myApp.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);

    @Query("{isActive: true}")
    List<User> findByIsActive();

    Long deleteByUsername(String userName);

    @Query("{ $or: [{username: ?0}, {username: ?1}]}")
    List<User> findUsersByUsernames(String userName_1, String userName_2);
}