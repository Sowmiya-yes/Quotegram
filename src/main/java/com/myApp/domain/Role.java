package com.myApp.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Setter(AccessLevel.PRIVATE)
    private String role;

    public static final Role USER_ROLE = new Role("USER");
    public static final Role SERVICE_ROLE = new Role("SERVICE");
}
