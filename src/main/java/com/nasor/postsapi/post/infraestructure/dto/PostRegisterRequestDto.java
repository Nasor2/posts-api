package com.nasor.postsapi.post.infraestructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request DTO for creating a new post.")
public record PostRegisterRequestDto(
        @Schema(description = "Title of the post", example = "My First Blog Post", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Title Required.")
        String title,

        @Schema(description = "Content of the post", example = "This is the amazing content of my first blog post.", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Content required.")
        @Size(min = 10)
        String content,

        @Schema(description = "ID of the user who owns the post", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "User ID required.")
        Long userId
) {
}
