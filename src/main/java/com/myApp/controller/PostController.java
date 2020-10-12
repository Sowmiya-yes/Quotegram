package com.myApp.controller;

import com.myApp.domain.Post;
import com.myApp.repository.PostRepository;
import com.myApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@RestController
public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;

//     to be modified to include sorting and follower
    @GetMapping("/posts")
    List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/post")
    List<Post> getPostByUserName(@RequestParam String postedBy) {
        return postRepository.findByPostedBy(postedBy);
    }

    @GetMapping("/post/{postId}")
    Post getPostById(@PathVariable String postId) {
        return postRepository.findById(postId)
                .orElse(null);
    }

    @PostMapping("/posts")
    Post createNewPost(@RequestBody Post newPost) {
        newPost.setNumberOfLikes(0);
        newPost.setPostedTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        return postRepository.save(newPost);
    }

    @PostMapping("/upload")
    String uploadFile(@RequestParam MultipartFile file) throws IOException {
        postService.uploadFile(file);
        return "Success";
    }

    @DeleteMapping("/posts/{postId}")
    boolean deletePostByPostId(@PathVariable String postId) {
        postRepository.deleteById(postId);
        return true;
    }
}
