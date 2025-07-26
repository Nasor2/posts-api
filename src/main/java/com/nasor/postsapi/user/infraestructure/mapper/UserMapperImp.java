package com.nasor.postsapi.user.infraestructure.mapper;

import com.nasor.postsapi.user.domain.User;
import com.nasor.postsapi.user.infraestructure.dto.UserDto;
import com.nasor.postsapi.user.infraestructure.dto.UserRequestDto;
import com.nasor.postsapi.user.infraestructure.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImp implements UserMapper {

    @Override
    public User entityToUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();
    }

    @Override
    public UserDto userToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFirstName() + " "+ user.getLastName())
                .build();
    }

    @Override
    public UserEntity userToUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public User updateDtoToUser(UserRequestDto userUpdateRequestDto) {
        return User.builder()
                .firstName(userUpdateRequestDto.firstName())
                .lastName(userUpdateRequestDto.lastName())
                .email(userUpdateRequestDto.email())
                .build();
    }
}
