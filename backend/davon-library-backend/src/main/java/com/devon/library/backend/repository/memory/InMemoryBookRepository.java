package com.devon.library.backend.repository.memory;

import com.devon.library.backend.model.Author;
import com.devon.library.backend.model.Book;
import com.devon.library.backend.repository.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryBookRepository implements BookRepository {

  private final Map<Long, Book> idToBook = new ConcurrentHashMap<>();
  private final AtomicLong idSequence = new AtomicLong(1);

  @Override
  public Book save(Book book) {
    if (book.getId() == null) {
      long id = idSequence.getAndIncrement();
      book.setId(id);
      // ensure author has an id too for consistency in UI listings
      Author author = book.getAuthor();
      if (author != null && author.getId() == null) {
        author.setId(idSequence.getAndIncrement());
        book.setAuthor(author);
      }
    }
    idToBook.put(book.getId(), book);
    return book;
  }

  @Override
  public Optional<Book> findById(Long id) {
    return Optional.ofNullable(idToBook.get(id));
  }

  @Override
  public List<Book> findAll() {
    return new ArrayList<>(idToBook.values());
  }

  @Override
  public List<Book> searchByTitleOrAuthor(String query) {
    if (query == null || query.isBlank()) {
      return findAll();
    }
    final String q = query.toLowerCase();
    return idToBook.values().stream()
        .filter(b ->
            (b.getTitle() != null && b.getTitle().toLowerCase().contains(q)) ||
            (b.getAuthor() != null && b.getAuthor().getName() != null && b.getAuthor().getName().toLowerCase().contains(q)) ||
            (b.getIsbn() != null && b.getIsbn().toLowerCase().contains(q)))
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    idToBook.remove(id);
  }
}


