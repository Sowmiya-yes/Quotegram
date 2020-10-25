package com.myApp.domain;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class SignupRequest {
    @NotBlank
    private String firstName;

    private String lastName;

    @NotBlank
    @Max(20)
    @Min(5)
    private String userName;

    @Email
    private String email;

    @NotBlank
    @Min(8)
    @Max(20)
    private String password;
}
