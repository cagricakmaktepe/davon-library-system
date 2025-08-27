package com.devon.library.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import com.devon.library.backend.repository.memory.InMemoryBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookServiceTest {

  private BookService bookService;

  @BeforeEach
  void setup() {
    bookService = new BookService();
    bookService.bookRepository = new InMemoryBookRepository();
  }

  @Test
  void addAndSearchBooks_shouldWork() {
    bookService.addBook("Clean Code", "ISBN1", 464, 3, "Robert C. Martin");
    bookService.addBook("Effective Java", "ISBN2", 416, 2, "Joshua Bloch");
    var results = bookService.search("clean");
    assertEquals(1, results.size());
    assertEquals("Clean Code", results.get(0).getTitle());
  }
}


