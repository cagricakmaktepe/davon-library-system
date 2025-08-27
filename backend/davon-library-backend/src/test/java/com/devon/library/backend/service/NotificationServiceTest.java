package com.devon.library.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import com.devon.library.backend.repository.memory.InMemoryNotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NotificationServiceTest {

  private NotificationService notificationService;

  @BeforeEach
  void setup() {
    notificationService = new NotificationService();
    notificationService.notificationRepository = new InMemoryNotificationRepository();
  }

  @Test
  void notifyUser_shouldCreateUnreadNotification() {
    var n = notificationService.notifyUser(1L, "INFO", "Title", "Message");
    assertNotNull(n.getId());
    assertEquals(1L, n.getUserId());
    assertEquals("INFO", n.getType());
    assertFalse(n.getRead());
    assertNotNull(n.getCreatedAt());
  }
  @Test
  void forUser_shouldReturnOnlyUserNotifications() {
    notificationService.notifyUser(1L, "INFO", "A", "M1");
    notificationService.notifyUser(2L, "WARN", "B", "M2");
    var list = notificationService.forUser(1L);
    assertEquals(1, list.size());
    assertEquals("A", list.get(0).getTitle());
  }
}




