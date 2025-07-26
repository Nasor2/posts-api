package com.nasor.postsapi.user.infraestructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Represents a simplified user profile for post responses.")
public record UserDto(
        @Schema(description = "User ID", example = "1")
        Long id,

        @Schema(description = "User Full Name", example = "Juan Gonzales")
        String fullName,

        @Schema(description = "User email", example = "emaple@gmail.com")
        String email)
{
}
