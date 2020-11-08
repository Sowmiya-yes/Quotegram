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
@Document(collection = "followee")
public class Followee {

    @Id
    String id;

    @NotNull
    String followeeId;

    @NotNull
    List<String> followersId;

    @CreatedDate
    Date createdAt;

    @LastModifiedDate
    Date lastModifiedDate;

    public Followee(String followeeId, List<String> followersId) {
        this.followeeId = followeeId;
        this.followersId =  followersId;
    }
}
