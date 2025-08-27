package com.devon.library.backend.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class NotificationTest {

  @Test
  void builder_shouldSetFields() {
    LocalDateTime now = LocalDateTime.now();
    Notification n = Notification.builder()
        .id(1L)
        .userId(2L)
        .type("INFO")
        .title("T")
        .message("M")
        .createdAt(now)
        .read(false)
        .build();
    assertEquals(2L, n.getUserId());
    assertEquals("INFO", n.getType());
    assertEquals("T", n.getTitle());
    assertEquals("M", n.getMessage());
    assertEquals(now, n.getCreatedAt());
    assertFalse(n.getRead());
  }
}



