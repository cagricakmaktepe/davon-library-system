package com.devon.library.backend.repository.memory;

import com.devon.library.backend.model.Loan;
import com.devon.library.backend.repository.LoanRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryLoanRepository implements LoanRepository {
  private final Map<Long, Loan> idToLoan = new ConcurrentHashMap<>();
  private final AtomicLong idSequence = new AtomicLong(1);

  @Override
  public Loan save(Loan loan) {
    if (loan.getId() == null) {
      loan.setId(idSequence.getAndIncrement());
    }
    idToLoan.put(loan.getId(), loan);
    return loan;
  }

  @Override
  public Optional<Loan> findById(Long id) {
    return Optional.ofNullable(idToLoan.get(id));
  }

  @Override
  public List<Loan> findByUserId(Long userId) {
    return idToLoan.values().stream().filter(l -> l.getUserId().equals(userId)).collect(Collectors.toList());
  }

  @Override
  public List<Loan> findByBookId(Long bookId) {
    return idToLoan.values().stream().filter(l -> l.getBookId().equals(bookId)).collect(Collectors.toList());
  }

  @Override
  public List<Loan> findAll() {
    return new ArrayList<>(idToLoan.values());
  }
}


