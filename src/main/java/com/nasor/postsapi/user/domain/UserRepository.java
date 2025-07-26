package com.nasor.postsapi.user.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    Optional<User> finByEmail(String email);
}
