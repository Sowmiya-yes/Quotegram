package com.myApp.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@ToString
@Document
public class User {

    @Id
    @Setter(AccessLevel.PRIVATE)
    private String userId;

    private String userName;
    private String userFirstName;
    private String userLastName;
    private List<String> userPostIds;
    private List<String> followerIds;
}
