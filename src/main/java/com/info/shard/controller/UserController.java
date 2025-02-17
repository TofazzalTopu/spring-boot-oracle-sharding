package com.info.shard.controller;

import com.info.shard.entity.User;
import com.info.shard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestParam Long userId, @RequestParam String name) {
        return ResponseEntity.ok(userService.saveUser(userId, name));
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long userId) {
        Optional<User> user = userService.getUser(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> findByAll() {
        List<User> userList = userService.findAll();
        return ResponseEntity.ok(userList);
    }

}

