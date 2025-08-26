package com.devon.library.backend.repository.memory;

import com.devon.library.backend.model.Notification;
import com.devon.library.backend.repository.NotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryNotificationRepository implements NotificationRepository {

  private final Map<Long, Notification> idToNotification = new ConcurrentHashMap<>();
  private final AtomicLong idSequence = new AtomicLong(1);

  @Override
  public Notification save(Notification notification) {
    if (notification.getId() == null) {
      notification.setId(idSequence.getAndIncrement());
    }
    idToNotification.put(notification.getId(), notification);
    return notification;
  }

  @Override
  public Optional<Notification> findById(Long id) {
    return Optional.ofNullable(idToNotification.get(id));
  }

  @Override
  public List<Notification> findByUserId(Long userId) {
    return idToNotification.values().stream()
        .filter(n -> n.getUserId().equals(userId))
        .collect(Collectors.toList());
  }

  @Override
  public List<Notification> findAll() {
    return new ArrayList<>(idToNotification.values());
  }

  @Override
  public void deleteById(Long id) {
    idToNotification.remove(id);
  }
}


