package com.nasor.postsapi.user.infraestructure.repository;

import com.nasor.postsapi.user.domain.User;
import com.nasor.postsapi.user.domain.UserRepository;
import com.nasor.postsapi.user.infraestructure.entity.UserEntity;
import com.nasor.postsapi.user.infraestructure.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SqlUserRepository implements UserRepository {

    private final SpringUserRepository springUserRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.userToUserEntity(user);
        UserEntity savedUser = springUserRepository.save(userEntity);

        return userMapper.entityToUser(savedUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        return springUserRepository.findById(id).map(userMapper::entityToUser);
    }

    @Override
    public List<User> findAll() {
        return springUserRepository.findAll()
                .stream()
                .map(userMapper::entityToUser)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Optional<User> finByEmail(String email) {
        return springUserRepository.findByEmail(email).map(userMapper::entityToUser);
    }
}
