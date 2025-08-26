package com.devon.library.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
  private Long id;
  private String title;
  private Author author;
  private String isbn;
  private int totalCopies;
  private int availableCopies;
  private int pageCount;
}


