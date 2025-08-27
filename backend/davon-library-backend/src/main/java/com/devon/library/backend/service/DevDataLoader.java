package com.devon.library.backend.service;

import com.devon.library.backend.model.Author;
import com.devon.library.backend.model.Book;
import com.devon.library.backend.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DevDataLoader {

  @Inject
  BookRepository bookRepository;

  @PostConstruct
  void seed() {
    if (!bookRepository.findAll().isEmpty()) {
      return;
    }

    bookRepository.save(Book.builder()
        .title("Clean Code")
        .isbn("9780132350884")
        .pageCount(464)
        .totalCopies(3)
        .availableCopies(3)
        .author(Author.builder().name("Robert C. Martin").build())
        .build());

    bookRepository.save(Book.builder()
        .title("Effective Java")
        .isbn("9780134685991")
        .pageCount(416)
        .totalCopies(2)
        .availableCopies(2)
        .author(Author.builder().name("Joshua Bloch").build())
        .build());

    bookRepository.save(Book.builder()
        .title("Domain-Driven Design")
        .isbn("9780321125217")
        .pageCount(560)
        .totalCopies(1)
        .availableCopies(1)
        .author(Author.builder().name("Eric Evans").build())
        .build());
  }
}


