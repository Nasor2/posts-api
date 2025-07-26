package com.nasor.postsapi.post.infraestructure.repository;

import com.nasor.postsapi.post.domain.Post;
import com.nasor.postsapi.post.infraestructure.entity.PostEntity;
import com.nasor.postsapi.post.infraestructure.mapper.PostMapper;
import com.nasor.postsapi.user.infraestructure.entity.UserEntity;
import com.nasor.postsapi.user.infraestructure.repository.SpringUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostPostgresRepository{

    private final SpringPostRepository springPostRepository;
    private final SpringUserRepository springUserRepository;
    private final PostMapper postMapper;

    @Transactional
    public Post save(Post post) {
        UserEntity existingUser = springUserRepository.findById(post.getUserId()).
                orElseThrow(() -> new RuntimeException("User not found"));

        PostEntity postEntity = postMapper.postToEntity(post, existingUser);

        PostEntity savedPost = springPostRepository.save(postEntity);

        return postMapper.entityToPost(savedPost);
    }

    @Transactional
    public Post update(Post post) {
        PostEntity existingPostEntity = springPostRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + post.getId()));

        UserEntity newAuthorEntity = springUserRepository.findById(post.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + post.getUserId()));

        existingPostEntity.setTitle(post.getTitle());
        existingPostEntity.setContent(post.getContent());
        existingPostEntity.setAuthor(newAuthorEntity);

        PostEntity updatedPostEntity = springPostRepository.save(existingPostEntity);

        return postMapper.entityToPost(updatedPostEntity);
    }

    public Optional<Post> findById(Long id) {
        return springPostRepository.findById(id).map(postMapper::entityToPost);
    }

    public List<Post> findAll() {
        return springPostRepository.findAll().stream().map(postMapper::entityToPost).collect(Collectors.toList());
    }

    public List<Post> findAllByUserId(Long userId) {
        return springPostRepository.findAllByAuthorId(userId).stream().map(postMapper::entityToPost).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        springPostRepository.deleteById(id);
    }

    public Optional<Long> findUserIdByPostId(Long postId) {
        return springPostRepository.findById(postId)
                .map(PostEntity::getAuthor)
                .map(UserEntity::getId);
    }
}
