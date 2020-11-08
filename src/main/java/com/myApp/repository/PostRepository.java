package com.myApp.repository;

import com.myApp.domain.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    @Query("{ 'username': ?0 }")
    List<Post> findByUsernameOrderByCreatedTSDesc(String username, Sort sort);
}
