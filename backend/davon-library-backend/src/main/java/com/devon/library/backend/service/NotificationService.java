package com.devon.library.backend.service;

import com.devon.library.backend.model.Notification;
import com.devon.library.backend.repository.NotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class NotificationService {

  @Inject
  NotificationRepository notificationRepository;

  public Notification notifyUser(Long userId, String type, String title, String message) {
    Notification n = Notification.builder()
        .userId(userId)
        .type(type)
        .title(title)
        .message(message)
        .createdAt(LocalDateTime.now())
        .read(false)
        .build();
    return notificationRepository.save(n);
  }

  public List<Notification> forUser(Long userId) {
    return notificationRepository.findByUserId(userId);
  }
}


