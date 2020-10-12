package com.myApp.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

@Data
public class Post {
    @Id
    @Setter(AccessLevel.PRIVATE)
    private String postId;
    private String postedBy;
    private String postedTimestamp;
    private int numberOfLikes;
    private String postPath;
}
