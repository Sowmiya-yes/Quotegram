package com.myApp.service;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mongodb.MongoException;
import com.myApp.config.AWSConfig;
import com.myApp.domain.Post;
import com.myApp.exception.ActionDeniedException;
import com.myApp.exception.PostNotFoundException;
import com.myApp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PostService {

    @Autowired
    AWSConfig awsConfig;
    @Autowired
    PostRepository postRepository;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();

        awsConfig.getAmazonS3Client().putObject(
                new PutObjectRequest(awsConfig.getBucketName(), multipartFile.getOriginalFilename(), file));

        return awsConfig.getBucketName() + multipartFile.getOriginalFilename();
    }

    public List<Post> getPostsByUsername(String username) {
        return postRepository.findByUsernameOrderByCreatedTSDesc(username);
    }

    public Post getPostByPostId(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post %s not found", postId)));
    }

    public Post createNewPost(Post post, MultipartFile file) {
        String postUrl;
        Post savedPost = new Post();
        try {
            postUrl = uploadFile(file);
            if(postUrl != null && !postUrl.equals("")) {
                post.setImageURL(postUrl);
                post.setLikesCount(0);
                savedPost = postRepository.save(post);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while uploading post to S3" + e.getLocalizedMessage());
        } catch (MongoException e) {
            throw new RuntimeException("Error occurred while saving the post" + e.getLocalizedMessage());
        }
        return savedPost;
    }

    public void deletePostByPostId(String postId, String userName) throws PostNotFoundException {
        Post postToDelete = getPostByPostId(postId);
        if(postToDelete != null && postToDelete.getUsername().equals(userName)) {
            postRepository.delete(postToDelete);
        } else {
            throw new ActionDeniedException("Not allowed to delete post" + postId);
        }
    }
}
