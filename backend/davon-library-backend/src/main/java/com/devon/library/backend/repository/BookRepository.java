package com.devon.library.backend.repository;

import com.devon.library.backend.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
  Book save(Book book);
  Optional<Book> findById(Long id);
  List<Book> findAll();
  List<Book> searchByTitleOrAuthor(String query);
  void deleteById(Long id);
}


