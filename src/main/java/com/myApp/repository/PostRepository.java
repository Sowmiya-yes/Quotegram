package com.myApp.repository;

import com.myApp.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findByUsernameOrderByCreatedTSDesc(String username);
}
