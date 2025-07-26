package com.nasor.postsapi.post.domain;

import java.util.List;
import java.util.Optional;

public interface PostService {
    List<Post> findAll();

    Optional<Post> findById(Long id);

    List<Post> findAllByUserId(Long id);

    Post save(Post post);

    Post update(Post post);

    void delete(Long id);
}
