package com.nasor.postsapi.user.domain;


import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    User update(User user);

    List<User> findAll();

    Optional<User> findById(Long id);
}
