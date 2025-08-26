package com.devon.library.backend.service;

import com.devon.library.backend.model.Loan;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class FineService {

  /**
   * Calculates progressive fine for an overdue loan.
   *  - First 30 overdue days: 0.5 per day
   *  - Beyond 30 days: 1.0 per day
   */
  public double calculateFine(Loan loan, LocalDate today) {
    if (loan == null || loan.getDueDate() == null) {
      return 0.0;
    }

    LocalDate comparisonDate = loan.getReturnDate() != null ? loan.getReturnDate() : today;
    long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), comparisonDate);
    if (daysOverdue <= 0) {
      return 0.0;
    }

    long firstTier = Math.min(30, daysOverdue);
    long secondTier = Math.max(0, daysOverdue - 30);
    return firstTier * 0.5 + secondTier * 1.0;
  }
}


