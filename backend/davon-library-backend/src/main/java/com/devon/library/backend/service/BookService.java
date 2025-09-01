package com.devon.library.backend.service;

import com.devon.library.backend.model.Author;
import com.devon.library.backend.model.Book;
import com.devon.library.backend.repository.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookService {

  @Inject
  BookRepository bookRepository;

  public Book addBook(String title, String isbn, int pageCount, int totalCopies, String authorName) {
    Author author = Author.builder().name(authorName).build();
    Book book = Book.builder()
        .title(title)
        .isbn(isbn)
        .pageCount(pageCount)
        .totalCopies(totalCopies)
        .availableCopies(totalCopies)
        .author(author)
        .build();
    return bookRepository.save(book);
  }

  public Optional<Book> getBook(Long id) {
    return bookRepository.findById(id);
  }

  public List<Book> listBooks() {
    return bookRepository.findAll();
  }

  public List<Book> search(String query) {
    return bookRepository.searchByTitleOrAuthor(query);
  }

  public Book updateBook(Long id, String title, String isbn, Integer pageCount, Integer totalCopies, String authorName) {
    Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found"));
    if (title != null) book.setTitle(title);
    if (isbn != null) book.setIsbn(isbn);
    if (pageCount != null) book.setPageCount(pageCount);
    if (authorName != null) {
      Author a = book.getAuthor();
      if (a == null) a = Author.builder().name(authorName).build(); else a.setName(authorName);
      book.setAuthor(a);
    }
    if (totalCopies != null) {
      int delta = totalCopies - book.getTotalCopies();
      book.setTotalCopies(totalCopies);
      book.setAvailableCopies(Math.max(0, book.getAvailableCopies() + delta));
    }
    return bookRepository.save(book);
  }

  public void deleteBook(Long id) {
    bookRepository.deleteById(id);
  }

  public Book setCopyCount(Long id, int totalCopies) {
    if (totalCopies < 0) throw new IllegalArgumentException("totalCopies must be >= 0");
    Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found"));
    int delta = totalCopies - book.getTotalCopies();
    book.setTotalCopies(totalCopies);
    book.setAvailableCopies(Math.max(0, book.getAvailableCopies() + delta));
    return bookRepository.save(book);
  }
}


