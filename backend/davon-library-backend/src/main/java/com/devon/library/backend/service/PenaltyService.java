package com.devon.library.backend.service;

import com.devon.library.backend.model.Loan;
import com.devon.library.backend.model.Penalty;
import com.devon.library.backend.repository.LoanRepository;
import com.devon.library.backend.repository.PenaltyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PenaltyService {

  @Inject
  PenaltyRepository penaltyRepository;

  @Inject
  LoanRepository loanRepository;

  @Inject
  FineService fineService;

  public Penalty ensurePenaltyForLoan(Long loanId) {
    Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
    double amount = fineService.calculateFine(loan, LocalDate.now());
    if (amount <= 0) {
      // No penalty needed
      return null;
    }
    List<Penalty> existing = penaltyRepository.findByLoanId(loanId);
    if (!existing.isEmpty()) {
      Penalty p = existing.get(0);
      p.setAmount(amount);
      return penaltyRepository.save(p);
    }
    Penalty p = Penalty.builder()
        .loanId(loanId)
        .userId(loan.getUserId())
        .amount(amount)
        .paid(false)
        .createdAt(LocalDateTime.now())
        .build();
    return penaltyRepository.save(p);
  }

  public List<Penalty> byUser(Long userId) {
    return penaltyRepository.findByUserId(userId);
  }

  public Penalty markPaid(Long penaltyId) {
    Penalty p = penaltyRepository.findById(penaltyId).orElseThrow(() -> new IllegalArgumentException("Penalty not found"));
    p.setPaid(true);
    p.setPaidAt(LocalDateTime.now());
    return penaltyRepository.save(p);
  }
}


