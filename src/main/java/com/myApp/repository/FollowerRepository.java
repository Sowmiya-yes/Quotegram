package com.myApp.repository;

import com.myApp.domain.Follower;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowerRepository extends MongoRepository<Follower, String> {
    Optional<Follower> findByFollowerId(String followerId);
}
