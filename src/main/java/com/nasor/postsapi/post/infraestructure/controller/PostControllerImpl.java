package com.nasor.postsapi.post.infraestructure.controller;

import com.nasor.postsapi.post.domain.Post;
import com.nasor.postsapi.post.domain.PostService;
import com.nasor.postsapi.post.infraestructure.dto.PostRegisterRequestDto;
import com.nasor.postsapi.post.infraestructure.dto.PostResponseDto;
import com.nasor.postsapi.post.infraestructure.dto.PostUpdateRequestDto;
import com.nasor.postsapi.post.infraestructure.mapper.PostMapper;
import com.nasor.postsapi.user.domain.User;
import com.nasor.postsapi.user.domain.UserService;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Operations related to blog posts")
public class PostControllerImpl implements PostController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @Override
    @Operation(summary = "Create a new post", description = "Creates a new post associated with an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or missing required fields.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\": \"Validation Error\", \"message\": \"Title must not be blank.\"}"))),
            @ApiResponse(responseCode = "404", description = "User not found for the given userId."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Post details for creation. Must include an existing userId.",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostRegisterRequestDto.class)))
            PostRegisterRequestDto postRegisterRequestDto) {
        User existingUser = userService.findById(postRegisterRequestDto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postMapper.registerPostToPost(postRegisterRequestDto, existingUser.getId());

        Post createdPost = postService.save(post);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPost.getId()).toUri();

        return ResponseEntity.created(location).body(postMapper.postToDto(createdPost, existingUser));
    }

    @Override
    @Operation(summary = "Update an existing post", description = "Updates the details of an existing post by its ID. The associated user cannot be changed directly via this endpoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or missing required fields.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\": \"Validation Error\", \"message\": \"Content must be at least 10 characters.\"}"))),
            @ApiResponse(responseCode = "404", description = "Post or User not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            @Parameter(description = "ID of the post to update.", required = true)
            @PathVariable Long id,
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated post details. The 'userId' provided must match an existing user.",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostUpdateRequestDto.class)))
            PostUpdateRequestDto postUpdateRequestDto) {
        Post updatedPost = postService.update(Post.builder()
                    .id(id)
                    .userId(postUpdateRequestDto.userId())
                    .title(postUpdateRequestDto.title())
                    .content(postUpdateRequestDto.content())
                .build());

        User existingUser = userService.findById(postUpdateRequestDto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(postMapper.postToDto(updatedPost, existingUser));
    }

    @Override
    @Operation(summary = "Get posts by user ID", description = "Retrieves a list of posts associated with a specific user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of posts retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found for the given userId."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUserId(
            @Parameter(description = "ID of the user to retrieve posts for.", required = true)
            @PathVariable Long id) {
        User  existingUser = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> posts = postService.findAllByUserId(id);

        List<PostResponseDto> response = posts
                .stream()
                .map(post -> postMapper.postToDto(post, existingUser))
                .toList();

        return ResponseEntity.ok(response);
    }

    @Override
    @Operation(summary = "Get post by ID", description = "Retrieves a single post by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Post not found for the given ID."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(
            @Parameter(description = "ID of the post to retrieve.", required = true)
            @PathVariable Long id) {
        Post post = postService.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        User userPost = userService.findById(post.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(postMapper.postToDto(post, userPost));
    }

    @Override
    @Operation(summary = "Delete a post by ID", description = "Deletes a post from the system by its unique ID. This operation cannot be undone.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully (No Content)."),
            @ApiResponse(responseCode = "404", description = "Post not found for the given ID."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "ID of the post to delete.", required = true)
            @PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @Operation(summary = "Get all posts", description = "Retrieves a list of all posts present in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of all posts.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(
                postService.findAll().stream()
                        .map(
                                post -> postMapper
                                        .postToDto(
                                                post,
                                                userService.findById(post.getUserId())
                                                        .orElse(null)
                                        )
                        )
                        .collect(Collectors.toList())
        );
    }
}
