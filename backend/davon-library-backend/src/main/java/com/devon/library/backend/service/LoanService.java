package com.devon.library.backend.service;

import com.devon.library.backend.model.Book;
import com.devon.library.backend.model.Loan;
import com.devon.library.backend.repository.BookRepository;
import com.devon.library.backend.repository.LoanRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LoanService {

  @Inject
  LoanRepository loanRepository;

  @Inject
  BookRepository bookRepository;

  public Loan checkout(Long userId, Long bookId) {
    Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
    if (book.getAvailableCopies() <= 0) {
      throw new IllegalStateException("No available copies for checkout");
    }
    int loanDays = book.getPageCount() > 300 ? 21 : 14; // business rule
    book.setAvailableCopies(book.getAvailableCopies() - 1);
    bookRepository.save(book);

    Loan loan = Loan.builder()
        .userId(userId)
        .bookId(bookId)
        .checkoutDate(LocalDate.now())
        .dueDate(LocalDate.now().plusDays(loanDays))
        .build();
    return loanRepository.save(loan);
  }

  public Loan returnBook(Long loanId) {
    Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
    if (loan.isReturned()) {
      return loan; // idempotent
    }
    loan.setReturnDate(LocalDate.now());
    loanRepository.save(loan);

    bookRepository.findById(loan.getBookId()).ifPresent(book -> {
      book.setAvailableCopies(book.getAvailableCopies() + 1);
      bookRepository.save(book);
    });
    return loan;
  }

  public Optional<Loan> getLoan(Long id) {
    return loanRepository.findById(id);
  }

  public List<Loan> loansByUser(Long userId) {
    return loanRepository.findByUserId(userId);
  }
}


