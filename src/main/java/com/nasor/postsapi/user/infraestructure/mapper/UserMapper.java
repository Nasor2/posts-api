package com.nasor.postsapi.user.infraestructure.mapper;

import com.nasor.postsapi.user.domain.User;
import com.nasor.postsapi.user.infraestructure.dto.UserDto;
import com.nasor.postsapi.user.infraestructure.dto.UserRequestDto;
import com.nasor.postsapi.user.infraestructure.entity.UserEntity;


public interface UserMapper {
    User entityToUser(UserEntity userEntity);
    UserDto userToUserDto(User user);
    UserEntity userToUserEntity(User user);
    User updateDtoToUser(UserRequestDto userUpdateRequestDto);
}
