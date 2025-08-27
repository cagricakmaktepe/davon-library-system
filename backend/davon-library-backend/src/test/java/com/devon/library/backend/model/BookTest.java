package com.devon.library.backend.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BookTest {

  @Test
  void builderAndGetters_shouldWork() {
    Author author = Author.builder().id(1L).name("Author").build();
    Book book = Book.builder()
        .id(2L)
        .title("Title")
        .author(author)
        .isbn("ISBN")
        .totalCopies(3)
        .availableCopies(2)
        .pageCount(123)
        .build();

    assertEquals(2L, book.getId());
    assertEquals("Title", book.getTitle());
    assertEquals("ISBN", book.getIsbn());
    assertEquals(3, book.getTotalCopies());
    assertEquals(2, book.getAvailableCopies());
    assertEquals(123, book.getPageCount());
    assertEquals("Author", book.getAuthor().getName());
  }
}



