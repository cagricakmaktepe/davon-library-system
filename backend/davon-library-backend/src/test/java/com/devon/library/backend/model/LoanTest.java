package com.devon.library.backend.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class LoanTest {

  @Test
  void isReturned_shouldReflectReturnDate() {
    Loan loan = Loan.builder().dueDate(LocalDate.now()).build();
    assertFalse(loan.isReturned());
    loan.setReturnDate(LocalDate.now());
    assertTrue(loan.isReturned());
  }
}



