package com.nasor.postsapi.post.infraestructure.repository;

import com.nasor.postsapi.post.infraestructure.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringPostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findAllByAuthorId(Long authorId);
}
