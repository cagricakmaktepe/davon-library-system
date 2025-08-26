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
}


