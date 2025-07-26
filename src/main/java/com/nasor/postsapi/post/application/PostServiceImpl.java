package com.nasor.postsapi.post.application;

import com.nasor.postsapi.post.domain.Post;
import com.nasor.postsapi.post.domain.PostService;
import com.nasor.postsapi.post.infraestructure.repository.PostPostgresRepository;
import com.nasor.postsapi.post.infraestructure.repository.RedisPostRepositoryImpl;
import com.nasor.postsapi.user.domain.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostPostgresRepository postRepository;
    private final UserRepository userRepository;
    private final RedisPostRepositoryImpl cachedPostRepository;

    @Override
    public List<Post> findAll() {
        List<Post> posts = cachedPostRepository.findAll();

        if (!posts.isEmpty()) {
            log.info("Found {} posts", posts.size());
            return posts;
        }

        posts = postRepository.findAll();
        if (!posts.isEmpty()) {
            cachedPostRepository.saveAll(posts);
        }

        log.info("Found {} posts in db", posts.size());

        return posts;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Optional<Post> optionalPost = cachedPostRepository.findById(id);

        if (optionalPost.isPresent()) {
            log.info("Found {} post", id);
            return optionalPost;
        }

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + id + " not found"));

        cachedPostRepository.save(post);
        log.info("Found {} post", id);

        return Optional.of(post);
    }

    @Override
    public List<Post> findAllByUserId(Long id) {

        List<Post> posts = cachedPostRepository.findAllByUserId(id);
        if (!posts.isEmpty()) {
            log.info("Found {} posts in database", posts.size());
            return posts;
        }

        if (userRepository.findById(id).isEmpty()) {
            throw new RuntimeException("User not found");
        }

        posts =  postRepository.findAllByUserId(id);
        if (!posts.isEmpty()) {
            cachedPostRepository.saveAll(posts);
            log.info("Loaded {} posts for user {} from DB and cached them.", posts.size(), id);
        }else{
            log.info("No posts found for user {} in DB.", id);
        }
        return posts;
    }

    @Override
    public Post save(Post post) {
        if (userRepository.findById(post.getUserId()).isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Post savedPost = postRepository.save(post);

        cachedPostRepository.save(savedPost);


        return savedPost;
    }

    @Override
    public Post update(Post post) {
        if (userRepository.findById(post.getUserId()).isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Post updatedPost = postRepository.update(post);

        cachedPostRepository.save(updatedPost);

        return updatedPost;
    }

    @Override
    public void delete(Long id) {
        if (postRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Post not found");
        }
        postRepository.deleteById(id);
        cachedPostRepository.deleteById(id);
    }
}
