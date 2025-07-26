package com.nasor.postsapi.post.domain;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    Optional<Post> findById(Long id);

    List<Post> findAll();

    List<Post> findAllByUserId(Long userId);

    void deleteById(Long id);

    void saveAll(List<Post> posts);
}
