package com.nasor.postsapi.user.infraestructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
        @Schema(description = "User first name.", example = "Juan")
        @NotBlank(message="First Name required.")
        String firstName,

        @Schema(description = "User last name", example = "Gonzales")
        @NotBlank(message="Last Name required.")
        String lastName,

        @Schema(description = "User email", example = "example@gmail.com")
        @Email(message = "Email required.")
        @NotBlank(message = "Email cannot be blank.")
        String email
) {
}
