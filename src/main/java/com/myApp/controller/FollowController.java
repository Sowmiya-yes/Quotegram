package com.myApp.controller;

import com.myApp.domain.Response;
import com.myApp.exception.FollowNotFoundException;
import com.myApp.exception.UserRelatedException;
import com.myApp.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class FollowController {

    @Autowired
    FollowService followService;

    @GetMapping("followers/{userName}")
    public ResponseEntity<?> getFollowersForUser(@PathVariable String userName) {
        try {
            return ResponseEntity.ok(followService.getFollowersForUser(userName));
        } catch (UserRelatedException | FollowNotFoundException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getLocalizedMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("following/{userName}")
    public ResponseEntity<?> getFolloweesForUser(@PathVariable String userName) {
        try {
            return ResponseEntity.ok(followService.getFolloweesForUser(userName));
        } catch (UserRelatedException | FollowNotFoundException e) {
            return new ResponseEntity<Response>(
                    new Response(false, e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("follow/{followee}")
    public ResponseEntity<?> followUser(@PathVariable String followee,
                                        @AuthenticationPrincipal Principal principal) {
        try {
            followService.createFollowRelationship(followee, principal.getName());
            return new ResponseEntity<Response>(
                    new Response(true, String.format("%s is now following %s", principal.getName(), followee)), HttpStatus.CREATED);
        } catch (UserRelatedException e) {
            return new ResponseEntity<Response>(new Response(false, e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("unfollow/{followee}")
    public ResponseEntity<?> unfollowUser(@PathVariable String followee,
                                          @AuthenticationPrincipal Principal principal) {
        try {
            followService.deleteFollowRelationship(followee, principal.getName());
            return new ResponseEntity<Response>(
                    new Response(true, String.format("%s is not following %s", principal.getName(), followee)), HttpStatus.NO_CONTENT);
        } catch (UserRelatedException e) {
            return new ResponseEntity<Response>(new Response(false, e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
