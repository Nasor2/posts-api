package com.nasor.postsapi.user.infraestructure.controller;

import com.nasor.postsapi.user.infraestructure.dto.UserDto;
import com.nasor.postsapi.user.infraestructure.dto.UserRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserController {
    ResponseEntity<UserDto> createUser(UserRequestDto userUpdateRequestDto);
    ResponseEntity<UserDto> findById(Long id);
    ResponseEntity<List<UserDto>> findAll();
    ResponseEntity<UserDto> update(Long id, UserRequestDto userDto);
}
