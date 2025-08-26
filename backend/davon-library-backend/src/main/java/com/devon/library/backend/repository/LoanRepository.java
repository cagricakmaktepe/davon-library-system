package com.devon.library.backend.repository;

import com.devon.library.backend.model.Loan;
import java.util.List;
import java.util.Optional;

public interface LoanRepository {
  Loan save(Loan loan);
  Optional<Loan> findById(Long id);
  List<Loan> findByUserId(Long userId);
  List<Loan> findByBookId(Long bookId);
  List<Loan> findAll();
}


