package com.myApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myApp.domain.Post;
import com.myApp.domain.Response;
import com.myApp.exception.ActionDeniedException;
import com.myApp.exception.PostNotFoundException;
import com.myApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

//  TESTED
    @GetMapping("/posts/{userName}")
    ResponseEntity<?> getPostsByUserName(@PathVariable String userName) {
        return ResponseEntity.ok(postService.getPostsByUsername(userName));
    }

//  TESTED
    @GetMapping("/post/{postId}")
    ResponseEntity<?> getPostById(@PathVariable String postId) {
        try {
            return ResponseEntity.ok(postService.getPostByPostId(postId));
        } catch (PostNotFoundException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

//    TESTED (see why URL is not returning and check for mediatype)
    @PostMapping(value = "/post",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<?> createNewPost(@RequestPart String newPost,
                                    @RequestPart MultipartFile file,
                                    @AuthenticationPrincipal Principal principal) {
        Post post = new Post();
        try {
            post = new ObjectMapper().readValue(newPost, Post.class);
            post = postService.createNewPost(post, file, principal.getName());
        } catch (Exception  e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity
                .created(URI.create(String.format("/posts/%s", post.getPostId())))
                .build();
    }

    @DeleteMapping("/post/{postId}")
    ResponseEntity<?> deletePostByPostId(@PathVariable String postId,
                                         @AuthenticationPrincipal Principal principal) {
        try {
            postService.deletePostByPostId(postId, principal.getName());
            return (ResponseEntity<?>) ResponseEntity.noContent().build();
        } catch (PostNotFoundException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ActionDeniedException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }
//    Delete from AWS as well
//    Download from AWS

    @PutMapping("/post/{postId}")
    ResponseEntity<?> increasePostLikeCount(@PathVariable String postId,
                                         @AuthenticationPrincipal Principal principal) {
        try {
            postService.increasePostLikesCount(postId, principal.getName());
            return (ResponseEntity<?>) ResponseEntity.noContent().build();
        } catch (PostNotFoundException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
