package com.info.shard.controller;

import com.info.shard.dto.UserDTO;
import com.info.shard.entity.User;
import com.info.shard.service.UserServiceForJdbcTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/users/JdbcTemplate")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserControllerForJdbcTemplate {

    @Autowired
    private UserServiceForJdbcTemplate saveUserUsingJdbcTemplate;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new user", description = "Adds a new user to the system")
    public ResponseEntity<?> save(@Valid @RequestBody UserDTO user) throws URISyntaxException {
        saveUserUsingJdbcTemplate.save(user);
        return ResponseEntity.created(new URI("/users/JdbcTemplate")).body("Successfully saved");
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find user by id", description = "Find a user by id")
    public ResponseEntity<?> findById(@PathVariable @NotNull Long userId) {
        User user = saveUserUsingJdbcTemplate.findById(userId);
        return ResponseEntity.ok(user);
    }

}

