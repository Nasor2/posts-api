package com.nasor.postsapi.post.infraestructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request DTO for updating an existing post.")
public record PostUpdateRequestDto(

        @Schema(description = "ID of the user who owns the post", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "User ID required.")
        Long userId,

        @Schema(description = "Title of the post", example = "Updated Blog Post Title", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Title Required.")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @Schema(description = "Content of the post", example = "This is the updated content of my first blog post. It's even more amazing now!", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Content required.")
        @Size(min = 10, message = "Content must be at least 10 characters")
        String content
) {
}
