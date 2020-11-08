package com.myApp.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document("NewsFeed")
public class NewsFeed implements Serializable {
    @Id
    String newsFeedId;

    String userId;

    List<String> postIdsFromFollowers;

    boolean isRedisCached;

    @CreatedDate
    Date createdDate;

    @LastModifiedDate
    Date lastUpdatedDate;

    public NewsFeed(String followeeId, ArrayList<String> followersPostIds, boolean isCached) {
        this.userId = followeeId;
        this.postIdsFromFollowers = followersPostIds;
        this.isRedisCached = isCached;
    }
}
