package com.nasor.postsapi.post.infraestructure.repository;

import com.nasor.postsapi.post.infraestructure.entity.PostCacheEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CachedPostRepository extends CrudRepository<PostCacheEntity, Long> {
    Iterable<PostCacheEntity> findAllByAuthorId(Long authorId);
}
