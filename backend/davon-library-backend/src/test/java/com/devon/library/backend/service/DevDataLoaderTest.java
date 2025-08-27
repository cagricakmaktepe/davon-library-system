package com.devon.library.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import com.devon.library.backend.repository.memory.InMemoryBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DevDataLoaderTest {

  private DevDataLoader devDataLoader;
  private InMemoryBookRepository bookRepository;

  @BeforeEach
  void setup() {
    devDataLoader = new DevDataLoader();
    bookRepository = new InMemoryBookRepository();
    devDataLoader.bookRepository = bookRepository;
  }

  @Test
  void seed_shouldInsertBooksOnlyWhenEmpty() {
    devDataLoader.seed();
    int firstCount = bookRepository.findAll().size();
    assertTrue(firstCount >= 3);

    devDataLoader.seed();
    int secondCount = bookRepository.findAll().size();
    assertEquals(firstCount, secondCount);
  }
}




