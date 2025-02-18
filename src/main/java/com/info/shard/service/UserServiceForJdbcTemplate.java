package com.info.shard.service;

import com.info.shard.config.RoutingDataSource;
import com.info.shard.dto.UserDTO;
import com.info.shard.entity.User;
import com.info.shard.mapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceForJdbcTemplate {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserServiceForJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional
    public void save(UserDTO user) {
        RoutingDataSource.setShard(determineShard(user.getId()));
        String sql = "INSERT INTO USER_TEST (id, user_name) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getId(), user.getName());
    }

    public User findById(Long userId) {
        RoutingDataSource.setShard(determineShard(userId));
        String sql = "SELECT * FROM USER_TEST WHERE ID = ?";
        User user = (User) jdbcTemplate.queryForObject(sql, new Object[]{userId}, new UserRowMapper());
        return user;
    }


    private String determineShard(Long userId) {
        return (userId % 2 == 0) ? "shardOne" : "shardTwo";
    }


}
