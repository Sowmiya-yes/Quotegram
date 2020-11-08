package com.myApp.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(collection = "Post")
public class Post {
    @Id
    @Setter(AccessLevel.PRIVATE)
    private String postId;

    @NotNull
    private String username;

    @NotNull
    private String imageURL;

    @Size(max = 100)
    private String caption;

    private int likesCount;

    @CreatedDate
    private Date createdTS;

    @LastModifiedDate
    private Date updatedTS;
}
