package com.nasor.postsapi.post.infraestructure.mapper;

import com.nasor.postsapi.post.domain.Post;
import com.nasor.postsapi.post.infraestructure.dto.PostRegisterRequestDto;
import com.nasor.postsapi.post.infraestructure.dto.PostResponseDto;
import com.nasor.postsapi.post.infraestructure.entity.PostCacheEntity;
import com.nasor.postsapi.post.infraestructure.entity.PostEntity;
import com.nasor.postsapi.user.domain.User;
import com.nasor.postsapi.user.infraestructure.entity.UserEntity;

public interface PostMapper {

    Post entityToPost(PostEntity postEntity);

    PostEntity postToEntity(Post post, UserEntity userEntity);

    PostResponseDto postToDto(Post post, User user);

    Post registerPostToPost(PostRegisterRequestDto requestDto, Long userId);

    Post postCacheEntityToPost(PostCacheEntity postCacheEntity);

    PostCacheEntity postToCacheEntity(Post post);
}
