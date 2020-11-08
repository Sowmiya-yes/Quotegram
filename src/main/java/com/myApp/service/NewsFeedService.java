package com.myApp.service;

import com.myApp.domain.NewsFeed;
import com.myApp.domain.Post;
import com.myApp.domain.User;
import com.myApp.repository.NewsFeedRepository;
import com.myApp.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsFeedService {
    @Autowired
    NewsFeedRepository newsFeedRepository;
    @Autowired
    RedisRepository redisRepository;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;

    List<Post> getNewsFeedForUser(@AuthenticationPrincipal Principal principal) {
        User loggedInUser = userService.getUserByUsername(principal.getName());
        return redisRepository.findById(loggedInUser.getUserId())
                .getPostIdsFromFollowers()
                .stream()
                .map(postId -> postService.getPostByPostId(postId))
                .collect(Collectors.toList());
    }

    @Async
    @Scheduled(initialDelay = 18000L, fixedDelay = 9000L)
    void updateNewsFeedCache() {
        final List<NewsFeed> newsFeedsToBeCached = newsFeedRepository.findAllEntriesNeedsToBeCache();
        if (newsFeedsToBeCached.size() > 0) {
            CompletableFuture<Boolean> completableFuture = CompletableFuture
                    .supplyAsync(() -> {
                        Map<String, NewsFeed> newsFeedMap = newsFeedsToBeCached.stream()
                                .collect(Collectors.toMap(NewsFeed::getUserId, newsFeed -> newsFeed));
                        redisRepository.saveAll(newsFeedMap);
                        return true;
                    })
                    .exceptionally(e -> {
                        log.info(String.format("Couldn't cache news feed as %s", e));
                        return false;
                    });
            completableFuture
                    .handle((ex, result) -> {
                        if (ex) {
                            log.info(String.format("Not updating the isRedisCached to false, %s", ex));
                            return false;
                        } else {
                            newsFeedsToBeCached.forEach(newsFeed -> newsFeed.setRedisCached(true));
                            newsFeedRepository.saveAll(newsFeedsToBeCached);
                            return true;
                        }
                    });
        }
    }
}
