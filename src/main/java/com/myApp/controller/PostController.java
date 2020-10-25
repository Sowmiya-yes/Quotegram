package com.myApp.controller;

import com.myApp.domain.Post;
import com.myApp.domain.Response;
import com.myApp.exception.ActionDeniedException;
import com.myApp.exception.PostNotFoundException;
import com.myApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@RestController
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping("/post/{userName}")
    ResponseEntity<?> getPostByUserName(@PathVariable String userName) {
        return ResponseEntity.ok(postService.getPostsByUsername(userName));
    }

    @GetMapping("/post/{postId}")
    ResponseEntity<?> getPostById(@PathVariable String postId) {
        try {
            return ResponseEntity.ok(postService.getPostByPostId(postId));
        } catch (PostNotFoundException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/posts")
    ResponseEntity<?> createNewPost(@Valid @RequestBody Post newPost,
                                    @RequestParam MultipartFile file) {
        Post post = new Post();
        try {
            post = postService.createNewPost(newPost, file);
        } catch (Exception  e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity
                .created(URI.create(String.format("/posts/%s", post.getPostId())))
                .build();
    }

    @DeleteMapping("/posts/{postId}")
    ResponseEntity<?> deletePostByPostId(@PathVariable String postId,
                                         @AuthenticationPrincipal Principal principal) {
        try {
            postService.deletePostByPostId(postId, principal.getName());
            return (ResponseEntity<?>) ResponseEntity.noContent();
        } catch (PostNotFoundException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ActionDeniedException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }
}
