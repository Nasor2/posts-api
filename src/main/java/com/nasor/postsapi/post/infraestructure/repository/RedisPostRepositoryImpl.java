package com.nasor.postsapi.post.infraestructure.repository;

import com.nasor.postsapi.post.domain.Post;
import com.nasor.postsapi.post.domain.PostRepository;
import com.nasor.postsapi.post.infraestructure.entity.PostCacheEntity;
import com.nasor.postsapi.post.infraestructure.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Repository
public class RedisPostRepositoryImpl implements PostRepository {

    private final CachedPostRepository cachedPostRepository;
    private final PostMapper postMapper;

    @Override
    public Post save(Post post) {
        PostCacheEntity postCacheEntity = cachedPostRepository.save(postMapper.postToCacheEntity(post));
        return postMapper.postCacheEntityToPost(postCacheEntity);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return cachedPostRepository.findById(id).map(postMapper::postCacheEntityToPost);
    }

    @Override
    public List<Post> findAll() {
        Iterable<PostCacheEntity> all = cachedPostRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false).map(postMapper::postCacheEntityToPost).collect(Collectors.toList());
    }

    @Override
    public List<Post> findAllByUserId(Long userId) {
        Iterable<PostCacheEntity> all = cachedPostRepository.findAllByAuthorId(userId);
        return StreamSupport.stream(all.spliterator(), false).map(postMapper::postCacheEntityToPost).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        cachedPostRepository.deleteById(id);
    }

    @Override
    public void saveAll(List<Post> posts) {
        cachedPostRepository.saveAll(posts.stream().map(postMapper::postToCacheEntity).collect(Collectors.toList()));
    }
}
