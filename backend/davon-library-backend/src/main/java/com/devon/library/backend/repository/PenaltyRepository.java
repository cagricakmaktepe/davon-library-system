package com.devon.library.backend.repository;

import com.devon.library.backend.model.Penalty;
import java.util.List;
import java.util.Optional;

public interface PenaltyRepository {
  Penalty save(Penalty penalty);
  Optional<Penalty> findById(Long id);
  List<Penalty> findByUserId(Long userId);
  List<Penalty> findByLoanId(Long loanId);
  List<Penalty> findAll();
  void deleteById(Long id);
}


