package com.nasor.postsapi.post.infraestructure.dto;

import com.nasor.postsapi.user.infraestructure.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Represents a blog post with its associated user.")
public record PostResponseDto(
        @Schema(description = "User ID", example = "101")
        Long id,

        @Schema(description = "Title of the post", example = "My First Blog Post")
        String title,

        @Schema(description = "Content of the post", example = "This is the amazing content of my first blog post.")
        String content,

        UserDto user,

        @Schema(description = "Timestamp when the post was created", example = "2025-07-26T14:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp when the post was last updated", example = "2025-07-26T15:00:00")
        LocalDateTime updatedAt
) {
}
