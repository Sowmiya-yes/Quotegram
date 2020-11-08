package com.myApp.repository;

import com.myApp.domain.Followee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FolloweeRepository extends MongoRepository<Followee, String> {

    Optional<Followee> findByFolloweeId(String followeeId);
}
