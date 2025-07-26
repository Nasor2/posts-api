package com.nasor.postsapi.post.infraestructure.mapper;

import com.nasor.postsapi.post.domain.Post;
import com.nasor.postsapi.post.infraestructure.dto.PostRegisterRequestDto;
import com.nasor.postsapi.post.infraestructure.dto.PostResponseDto;
import com.nasor.postsapi.post.infraestructure.entity.PostCacheEntity;
import com.nasor.postsapi.post.infraestructure.entity.PostEntity;
import com.nasor.postsapi.user.domain.User;
import com.nasor.postsapi.user.infraestructure.dto.UserDto;
import com.nasor.postsapi.user.infraestructure.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostMapperImpl implements PostMapper {


    @Override
    public Post entityToPost(PostEntity postEntity) {
        return Post.builder()
                .id(postEntity.getId())
                .userId(postEntity.getAuthor().getId())
                .content(postEntity.getContent())
                .title(postEntity.getTitle())
                .createdDate(postEntity.getCreatedAt())
                .updatedDate(postEntity.getUpdatedAt())
                .build();
    }

    @Override
    public PostEntity postToEntity(Post post, UserEntity userEntity) {
        return PostEntity.builder()
                .id(post.getId())
                .author(userEntity)
                .content(post.getContent())
                .title(post.getTitle())
                .build();
    }

    @Override
    public PostResponseDto postToDto(Post post, User user) {
        return PostResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .user(
                        new UserDto(user.getId(),
                                user.getFirstName() + " " + user.getLastName(),
                                user.getEmail())
                )
                .title(post.getTitle())
                .createdAt(post.getCreatedDate())
                .updatedAt(post.getUpdatedDate())
                .build();
    }

    @Override
    public Post registerPostToPost(PostRegisterRequestDto requestDto, Long userId) {
        return Post.builder()
                .content(requestDto.content())
                .title(requestDto.title())
                .userId(userId)
                .build();
    }

    @Override
    public Post postCacheEntityToPost(PostCacheEntity postCacheEntity) {
        return Post.builder()
                .id(postCacheEntity.getId())
                .userId(postCacheEntity.getAuthorId())
                .content(postCacheEntity.getContent())
                .title(postCacheEntity.getTitle())
                .createdDate(postCacheEntity.getCreatedAt())
                .updatedDate(postCacheEntity.getUpdatedAt())
                .build();
    }

    @Override
    public PostCacheEntity postToCacheEntity(Post post) {
        return PostCacheEntity.builder()
                .id(post.getId())
                .authorId(post.getUserId())
                .content(post.getContent())
                .title(post.getTitle())
                .createdAt(post.getCreatedDate())
                .updatedAt(post.getUpdatedDate())
                .build();
    }

}
