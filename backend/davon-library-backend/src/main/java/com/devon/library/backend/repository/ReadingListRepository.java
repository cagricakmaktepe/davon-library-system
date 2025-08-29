package com.devon.library.backend.repository;

import com.devon.library.backend.model.ReadingListItem;
import java.util.List;

public interface ReadingListRepository {
  ReadingListItem save(ReadingListItem item);
  List<ReadingListItem> findByUserId(Long userId);
  void deleteById(Long id);
  void deleteByUserIdAndBookId(Long userId, Long bookId);
}


