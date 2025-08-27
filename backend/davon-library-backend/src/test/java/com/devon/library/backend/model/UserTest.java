package com.devon.library.backend.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserTest {

  @Test
  void builderAndSetters_shouldWork() {
    User user = User.builder().id(1L).name("Alice").email("alice@example.com").build();
    assertEquals(1L, user.getId());
    assertEquals("Alice", user.getName());
    assertEquals("alice@example.com", user.getEmail());

    user.setName("A");
    user.setEmail("a@e.com");
    assertEquals("A", user.getName());
    assertEquals("a@e.com", user.getEmail());
  }
}



