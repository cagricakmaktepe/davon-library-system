package com.devon.library.backend.repository;

import com.devon.library.backend.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
  Notification save(Notification notification);
  Optional<Notification> findById(Long id);
  List<Notification> findByUserId(Long userId);
  List<Notification> findAll();
  void deleteById(Long id);
}


