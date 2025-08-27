package com.devon.library.backend.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AuthorTest {

  @Test
  void builder_shouldSetFields() {
    Author a = Author.builder().id(1L).name("Robert C. Martin").build();
    assertEquals(1L, a.getId());
    assertEquals("Robert C. Martin", a.getName());
  }
}



