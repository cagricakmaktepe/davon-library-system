package com.devon.library.backend.repository.memory;

import com.devon.library.backend.model.ReadingListItem;
import com.devon.library.backend.repository.ReadingListRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryReadingListRepository implements ReadingListRepository {

  private final Map<Long, ReadingListItem> idToItem = new ConcurrentHashMap<>();
  private final AtomicLong idSequence = new AtomicLong(1);

  @Override
  public ReadingListItem save(ReadingListItem item) {
    if (item.getId() == null) {
      item.setId(idSequence.getAndIncrement());
    }
    idToItem.put(item.getId(), item);
    return item;
  }

  @Override
  public List<ReadingListItem> findByUserId(Long userId) {
    return idToItem.values().stream().filter(i -> i.getUserId().equals(userId)).collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    idToItem.remove(id);
  }

  @Override
  public void deleteByUserIdAndBookId(Long userId, Long bookId) {
    List<Long> toRemove = new ArrayList<>();
    for (ReadingListItem i : idToItem.values()) {
      if (i.getUserId().equals(userId) && i.getBookId().equals(bookId)) {
        toRemove.add(i.getId());
      }
    }
    for (Long id : toRemove) {
      idToItem.remove(id);
    }
  }
}


