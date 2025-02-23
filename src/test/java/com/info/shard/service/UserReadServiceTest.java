package com.info.shard.service;

import com.info.shard.entity.User;
import com.info.shard.repository.shard1.UserRepositoryShardOne;
import com.info.shard.repository.shard2.UserRepositoryShardTwo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserReadServiceTest {

    @Mock
    private UserRepositoryShardOne userRepositoryShardOne;
    @Mock
    private UserRepositoryShardTwo userRepositoryShardTwo;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setName("Tofazzal");


        user2 = new User();
        user2.setId(2L);
        user2.setName("Rana");
    }

    @Test
    void getUser_OddUserId_ReturnsUserFromShardOne() {
        when(userRepositoryShardTwo.findById(1L)).thenReturn(Optional.of(user1));
        Optional<User> result = userService.getUser(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Tofazzal", result.get().getName());
        verify(userRepositoryShardTwo, times(1)).findById(1L);
    }

    @Test
    void when_GetUser_NotFound_ForOddUserId_FromShardTwo() {
        when(userRepositoryShardTwo.findById(1L)).thenReturn(Optional.empty());
        Optional<User> result = userService.getUser(1L);
        assertFalse(result.isPresent());
        verify(userRepositoryShardTwo, times(1)).findById(1L);
    }

    @Test
    void getUser_EvenUserId_ReturnsUserFromShardOne() {
        when(userRepositoryShardOne.findById(2L)).thenReturn(Optional.of(user2));
        Optional<User> result = userService.getUser(2L);
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals("Rana", result.get().getName());
        verify(userRepositoryShardOne, times(1)).findById(2L);
    }

    @Test
    void when_GetUser_NotFound_ForEvenUserId_FromShardOne() {
        when(userRepositoryShardOne.findById(2L)).thenReturn(Optional.empty());
        Optional<User> result = userService.getUser(2L);
        assertFalse(result.isPresent());
        verify(userRepositoryShardOne, times(1)).findById(2L);
    }

    @Test
    void testFindAll() {
        List<User> users = List.of(user2);
        when(userRepositoryShardTwo.findAll(Sort.by("id").ascending())).thenReturn(users);
        List<User> result = userService.findAll();
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
        verify(userRepositoryShardTwo, times(1)).findAll(Sort.by("id").ascending());
    }


    @Test
    void findAll_InvokesRepositoryWithCorrectSortOrder() {
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(userRepositoryShardTwo.findAll(any(Sort.class))).thenReturn(mockUsers);
        List<User> result = userService.findAll();
        assertEquals(mockUsers, result);

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(userRepositoryShardTwo).findAll(sortCaptor.capture());

        Sort capturedSort = sortCaptor.getValue();
        assertNotNull(capturedSort.getOrderFor("id"));
        assertEquals(Sort.Direction.ASC, capturedSort.getOrderFor("id").getDirection());
    }


    @Test
    void get_UserRepository_When_EvenUserId_ReturnsShardOne() {
        CrudRepository repository = userService.userRepository(2L);
        assertEquals(userRepositoryShardOne, repository);
    }

    @Test
    void userRepository_NotFound_When_EvenUserId_ReturnsShardTwo() {
        CrudRepository repository = userService.userRepository(2L);
        assertNotEquals(userRepositoryShardTwo, repository);
    }

    @Test
    void get_UserRepository_when_OddUserId_ReturnsShardTwo() {
        CrudRepository repository = userService.userRepository(3L);
        assertEquals(userRepositoryShardTwo, repository);
    }

    @Test
    void get_UserRepository_NotFound_when_OddUserId_ReturnsShardOne() {
        CrudRepository repository = userService.userRepository(3L);
        assertNotEquals(userRepositoryShardOne, repository);
    }
}
