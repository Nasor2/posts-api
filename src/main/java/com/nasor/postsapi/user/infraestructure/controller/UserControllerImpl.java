package com.nasor.postsapi.user.infraestructure.controller;

import com.nasor.postsapi.user.domain.User;
import com.nasor.postsapi.user.domain.UserService;
import com.nasor.postsapi.user.infraestructure.dto.UserDto;
import com.nasor.postsapi.user.infraestructure.dto.UserRequestDto;
import com.nasor.postsapi.user.infraestructure.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operations related to user management")
public class UserControllerImpl implements UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    @PostMapping
    @Operation(summary = "Create a new user", description = "Registers a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully. Returns the created user and a location header.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or missing required fields.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\": \"Validation Error\", \"message\": \"Email must be a valid email format.\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<UserDto> createUser(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User details for creation. Requires first name, last name, and a valid email.",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDto.class)))
            UserRequestDto userDto)
    {
        User domainUser = userMapper.updateDtoToUser(userDto);

        User createdUser = userService.createUser(domainUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdUser.getId()).toUri();

        return ResponseEntity.created(location).body(userMapper.userToUserDto(createdUser));
    }

    @Override
    @Operation(summary = "Get user by ID", description = "Retrieves a single user by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found for the given ID."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(
            @Parameter(description = "ID of the user to retrieve.", required = true, example = "1")
            @PathVariable Long id
    ) throws RuntimeException
    {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found."));

        UserDto userDto = userMapper.userToUserDto(user);

        return ResponseEntity.ok(userDto);
    }

    @Override
    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of all users.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> usersDto = userService.findAll().stream().map(userMapper::userToUserDto).collect(Collectors.toList());

        return ResponseEntity.ok(usersDto);
    }

    @Override
    @Operation(summary = "Update an existing user", description = "Updates the details of an existing user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or missing required fields.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\": \"Validation Error\", \"message\": \"Email must be a valid email format.\"}"))),
            @ApiResponse(responseCode = "404", description = "User not found for the given ID."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @PathVariable
            @Parameter(description = "Unique identifier of the user to update.", required = true, example = "1")
            Long id,
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated user details. Requires first name, last name, and a valid email.",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDto.class)))
            UserRequestDto  userUpdateRequestDto
    )
    {
        User updated = userService.update(User.builder()
                .id(id)
                .firstName(userUpdateRequestDto.firstName())
                .email(userUpdateRequestDto.email())
                .lastName(userUpdateRequestDto.lastName())
                .build());

        UserDto userDto = userMapper.userToUserDto(updated);

        return ResponseEntity.ok(userDto);
    }
}
