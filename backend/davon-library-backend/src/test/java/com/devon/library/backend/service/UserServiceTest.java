package com.devon.library.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import com.devon.library.backend.repository.memory.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

  private UserService userService;

  @BeforeEach
  void setup() {
    userService = new UserService();
    userService.userRepository = new InMemoryUserRepository();
  }

  @Test
  void createAndFindUser_shouldWork() {
    var user = userService.createUser("Alice", "alice@example.com");
    assertNotNull(user.getId());
    var found = userService.findByEmail("alice@example.com").orElseThrow();
    assertEquals("Alice", found.getName());
  }
}


