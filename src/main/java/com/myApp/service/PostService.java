package com.myApp.service;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mongodb.MongoException;
import com.myApp.config.AWSConfig;
import com.myApp.domain.Follower;
import com.myApp.domain.NewsFeed;
import com.myApp.domain.Post;
import com.myApp.exception.ActionDeniedException;
import com.myApp.exception.PostNotFoundException;
import com.myApp.repository.FollowerRepository;
import com.myApp.repository.NewsFeedRepository;
import com.myApp.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {

    @Autowired
    AWSConfig awsConfig;
    @Autowired
    PostRepository postRepository;
    @Autowired
    NewsFeedRepository newsFeedRepository;
    @Autowired
    FollowService followService;

//    TESTED
    public String uploadFile(MultipartFile multipartFile, String userName) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        if(originalFilename != null && !originalFilename.equals("")) {
            File file = new File(originalFilename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            String awsDirectoryUrl = userName + "/" + multipartFile.getOriginalFilename();

            awsConfig.getAmazonS3Client().putObject(
                    new PutObjectRequest(awsConfig.getBucketName(), awsDirectoryUrl, file));

            return awsConfig.getBucketName() + "/" + awsDirectoryUrl;
        } else {
            throw new FileNotFoundException("File uploaded is not found");
        }
    }

//  TESTED
    public List<Post> getPostsByUsername(String username) {
        return postRepository.findByUsernameOrderByCreatedTSDesc(
                username, Sort.by( "createdTS").descending());
    }

//  TESTED
    public Post getPostByPostId(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post %s not found", postId)));
    }

//    TESTED
    public Post createNewPost(Post post, MultipartFile file, String userName) {
        String postUrl;
        Post savedPost = new Post();
        try {
            postUrl = uploadFile(file, userName);
            if(postUrl != null && !postUrl.equals("")) {
                post.setUsername(userName);
                post.setImageURL(postUrl);
                post.setLikesCount(0);
                savedPost = postRepository.save(post);

                savePostToNewFeed(savedPost);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while uploading post to S3" + e.getLocalizedMessage());
        } catch (MongoException e) {
            throw new RuntimeException("Error occurred while saving the post" + e.getLocalizedMessage());
        }
        return savedPost;
    }

    private void savePostToNewFeed(Post savedPost) {
        CompletableFuture.runAsync(() -> {
            Follower followers = followService.getFolloweesForUser(savedPost.getUsername());
            followers.getFolloweesId()
                    .forEach(followeeId -> {
                        newsFeedRepository.findByUserId(followeeId)
                            .ifPresentOrElse(newsFeed -> {
                                newsFeed.getPostIdsFromFollowers().add(savedPost.getPostId());
                                newsFeed.setRedisCached(false);
                                newsFeedRepository.save(newsFeed);
                            }, () -> {
                                NewsFeed updatedNewsFeed = new NewsFeed(followeeId, new ArrayList<String>(), false);
                                updatedNewsFeed.getPostIdsFromFollowers().add(savedPost.getPostId());
                                newsFeedRepository.save(updatedNewsFeed);
                            });
                    });
            }
        ).exceptionally(e -> {
            log.info(String.format("Couldn't process new feed for the followers of %s", savedPost.getUsername()));
            return null;
        });
    }

    //    TESTED
    public void deletePostByPostId(String postId, String userName) throws PostNotFoundException {
        Post postToDelete = getPostByPostId(postId);
        if(postToDelete != null && postToDelete.getUsername().equals(userName)) {
            postRepository.delete(postToDelete);
        } else {
            throw new ActionDeniedException("Not allowed to delete post" + postId);
        }
    }

//    TESTED
    public void increasePostLikesCount(String postId, String userName) {
        Post postToLike = getPostByPostId(postId);
        if(postToLike != null) {
            postToLike.setLikesCount(postToLike.getLikesCount() + 1);
            postRepository.save(postToLike);
        } else {
            throw new PostNotFoundException(String.format("Post %s not found", postId));
        }
    }
}
