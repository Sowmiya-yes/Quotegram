package com.myApp.service;

import com.myApp.domain.Followee;
import com.myApp.domain.Follower;
import com.myApp.domain.User;
import com.myApp.exception.FollowNotFoundException;
import com.myApp.exception.UserRelatedException;
import com.myApp.repository.FolloweeRepository;
import com.myApp.repository.FollowerRepository;
import com.myApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    FolloweeRepository followeeRepository;
    @Autowired
    FollowerRepository followerRepository;
    @Autowired
    UserRepository userRepository;

//    TESTED
    @Transactional
    public void createFollowRelationship(String followeeName, String followerName) {
        if (!StringUtils.isEmpty(followeeName) && !StringUtils.isEmpty(followerName)) {
            List<User> followerFolloweeUserObjects = userRepository.findUsersByUsernames(followeeName, followerName);
            if(followerFolloweeUserObjects.size() == 2) {
                Map<String, String> userNameUserIdMap = followerFolloweeUserObjects.stream()
                        .collect(Collectors.toMap(User::getUsername, User::getUserId));

                // Adds new follower to the list of followers for a particular followee
                Followee followee = followeeRepository.findByFolloweeId(userNameUserIdMap.get(followeeName))
                        .orElse(new Followee(userNameUserIdMap.get(followeeName), new ArrayList<>()));
                followee.getFollowersId().add(userNameUserIdMap.get(followerName));
                followeeRepository.save(followee);

                // Adds new followee to the list of followees for a particular follower
                Follower follower = followerRepository.findByFollowerId(userNameUserIdMap.get(followerName))
                        .orElse(new Follower(userNameUserIdMap.get(followerName), new ArrayList<>()));
                follower.getFolloweesId().add(userNameUserIdMap.get(followeeName));
                followerRepository.save(follower);
            } else {
                throw new UserRelatedException("Either follower or followee not found");
            }
        } else {
            throw new UserRelatedException("Either follower or followee name is empty");
        }
    }

//    TESTED
    public Followee getFollowersForUser(String followeeName) {
        Optional<User> followeeUserObject = userRepository.findByUsername(followeeName);
        if(followeeUserObject.isPresent()) {
            return followeeRepository.findByFolloweeId(followeeUserObject.get().getUserId())
                    .orElseThrow(() -> new FollowNotFoundException(String.format("No followers found for %s", followeeName)));
        } else {
            throw new UserRelatedException("Followee not found");
        }
    }

//    TESTED
    public Follower getFolloweesForUser(String followerName) {
        Optional<User> followerUserObject = userRepository.findByUsername(followerName);
        if(followerUserObject.isPresent()) {
            return followerRepository.findByFollowerId(followerUserObject.get().getUserId())
                    .orElseThrow(() -> new FollowNotFoundException(String.format("No followees found for %s", followerName)));
        } else {
            throw new UserRelatedException("Follower not found");
        }
    }

//    TESTED
    public void deleteFollowRelationship(String followeeName, String followerName) {
        if (!StringUtils.isEmpty(followeeName) && !StringUtils.isEmpty(followerName)) {
            List<User> followerFolloweeUserObjects = userRepository.findUsersByUsernames(followeeName, followerName);
            if(followerFolloweeUserObjects.size() == 2) {
                Map<String, String> userNameUserIdMap = followerFolloweeUserObjects.stream()
                        .collect(Collectors.toMap(User::getUsername, User::getUserId));

                // Adds a follower from the list of followers for a particular followee
                Followee followee = followeeRepository.findByFolloweeId(userNameUserIdMap.get(followeeName))
                        .orElse(new Followee(userNameUserIdMap.get(followeeName), new ArrayList<>()));
                followee.getFollowersId().remove(userNameUserIdMap.get(followerName));
                if(followee.getFollowersId().size() < 1)
                    followeeRepository.delete(followee);
                else
                    followeeRepository.save(followee);

                // Deletes a followee from the list of followees for a particular follower
                Follower follower = followerRepository.findByFollowerId(userNameUserIdMap.get(followerName))
                        .orElse(new Follower(userNameUserIdMap.get(followerName), new ArrayList<>()));
                follower.getFolloweesId().remove(userNameUserIdMap.get(followeeName));
                if(follower.getFolloweesId().size() < 1)
                    followerRepository.delete(follower);
                else
                    followerRepository.save(follower);
            } else {
                throw new UserRelatedException("Either follower or followee not found");
            }
        } else {
            throw new UserRelatedException("Either follower or followee name is empty");
        }
    }
}
