package com.devon.library.backend.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
  private Long id;
  private Long userId;
  private Long bookId;
  private LocalDate checkoutDate;
  private LocalDate dueDate;
  private LocalDate returnDate;

  public boolean isReturned() {
    return returnDate != null;
  }
}


