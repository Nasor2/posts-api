package com.nasor.postsapi.post.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Post {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

