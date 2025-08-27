package com.devon.library.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.devon.library.backend.model.Loan;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FineServiceTest {

  private FineService fineService;

  @BeforeEach
  void setup() {
    fineService = new FineService();
  }

  @Test
  void calculateFine_shouldBeZeroWhenNotOverdue() {
    Loan loan = Loan.builder()
        .dueDate(LocalDate.now().plusDays(1))
        .build();
    double fine = fineService.calculateFine(loan, LocalDate.now());
    assertEquals(0.0, fine);
  }

  @Test
  void calculateFine_shouldApplyProgressiveRates() {
    Loan loan = Loan.builder()
        .dueDate(LocalDate.now().minusDays(40))
        .build();
    double fine = fineService.calculateFine(loan, LocalDate.now());
    // 30 * 0.5 + 10 * 1.0 = 15 + 10 = 25
    assertEquals(25.0, fine, 0.001);
  }

  @Test
  void calculateFine_shouldUseReturnDateIfReturned() {
    Loan loan = Loan.builder()
        .dueDate(LocalDate.now().minusDays(5))
        .returnDate(LocalDate.now().minusDays(2))
        .build();
    double fine = fineService.calculateFine(loan, LocalDate.now());
    // 3 days overdue at 0.5 = 1.5
    assertEquals(1.5, fine, 0.001);
  }
}


