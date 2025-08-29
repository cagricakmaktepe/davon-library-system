package com.devon.library.backend.service;

import com.devon.library.backend.model.ReadingListItem;
import com.devon.library.backend.repository.ReadingListRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ReadingListService {

  @Inject
  ReadingListRepository repository;

  public ReadingListItem add(Long userId, Long bookId, Integer priority) {
    // Prevent duplicates
    boolean exists = repository.findByUserId(userId).stream().anyMatch(i -> i.getBookId().equals(bookId));
    if (exists) {
      return repository.findByUserId(userId).stream().filter(i -> i.getBookId().equals(bookId)).findFirst().get();
    }
    ReadingListItem item = ReadingListItem.builder()
        .userId(userId)
        .bookId(bookId)
        .priority(priority == null ? 1 : priority)
        .addedAt(LocalDateTime.now())
        .build();
    return repository.save(item);
  }

  public List<ReadingListItem> list(Long userId) {
    return repository.findByUserId(userId);
  }

  public void remove(Long userId, Long bookId) {
    repository.deleteByUserIdAndBookId(userId, bookId);
  }
}


