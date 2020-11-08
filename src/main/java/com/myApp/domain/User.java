package com.myApp.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
@Document(collection = "User")
public class User {

    public User(User user) {
        this.userId = user.userId;
        this.username = user.username;
        this.userFirstName = user.userFirstName;
        this.userLastName = user.userLastName;
        this.email = user.email;
        this.password = user.password;
        this.createdTS = user.createdTS;
        this.updatedTS = user.updatedTS;
        this.isActive = user.isActive;
        this.roles = user.roles;
    }

    @Id
    @Setter(AccessLevel.PRIVATE)
    private String userId;

    @NotBlank
    @Field("userName")
    private String username;

    @NotBlank
    @Field("firstName")
    private String userFirstName;

    @Field("lastName")
    private String userLastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @CreatedDate
    private Date createdTS;

    @LastModifiedDate
    private Date updatedTS;

    private boolean isActive;

    private String roles;
}
