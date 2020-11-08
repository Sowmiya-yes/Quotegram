package com.myApp.repository;

import com.myApp.domain.NewsFeed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NewsFeedRepository extends MongoRepository<NewsFeed, String> {
    @Query("{ 'userId': ?0 }")
    Optional<NewsFeed> findByUserId(String followeeId);

    @Query("{ 'isRedisCached': false}")
    List<NewsFeed> findAllEntriesNeedsToBeCache();
}
