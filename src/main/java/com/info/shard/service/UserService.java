package com.info.shard.service;

import com.info.shard.entity.User;
import com.info.shard.repository.shard1.UserRepositoryShardOne;
import com.info.shard.repository.shard2.UserRepositoryShardTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepositoryShardOne userRepositoryShardOne;

    private final UserRepositoryShardTwo userRepositoryShardTwo;

    @Autowired
    public UserService(UserRepositoryShardOne userRepositoryShardOne, UserRepositoryShardTwo userRepositoryShardTwo) {
        this.userRepositoryShardOne = userRepositoryShardOne;
        this.userRepositoryShardTwo = userRepositoryShardTwo;
    }

    @Transactional
    public User saveUser(Long userId, String name) {
        User user = new User(userId, name);
        return (User) userRepository(userId).save(user);
    }

    private String determineShard(Long userId) {
        return (userId <= 1000) ? "shard1" : "shard2";
    }

    public Optional<User> getUser(Long userId) {
        return userRepository(userId).findById(userId);
    }

    public List<User> findAll() {
        return userRepositoryShardTwo.findAll(Sort.by("id").ascending());
    }

    public <T> CrudRepository userRepository(Long userId) {
        return userId % 2 == 0 ? userRepositoryShardOne : userRepositoryShardTwo;
    }
}
