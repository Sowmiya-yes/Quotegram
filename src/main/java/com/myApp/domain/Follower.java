package com.myApp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "follower")
public class Follower {
    @Id
    String id;

    @NotNull
    String followerId;

    @NotNull
    List<String> followeesId;

    @CreatedDate
    Date createdAt;

    @LastModifiedDate
    Date lastModifiedDate;

    public Follower(String followerId, List<String> followeesId) {
        this.followerId = followerId;
        this.followeesId =  followeesId;
    }
}
