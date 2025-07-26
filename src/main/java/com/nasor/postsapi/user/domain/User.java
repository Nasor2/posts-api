package com.nasor.postsapi.user.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
