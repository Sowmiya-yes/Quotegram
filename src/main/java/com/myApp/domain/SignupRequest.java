package com.myApp.domain;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SignupRequest {
    @NotBlank
    private String firstName;

    private String lastName;

    @NotBlank
    @Size(min = 5, max = 20)
    private String userName;

    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
}
