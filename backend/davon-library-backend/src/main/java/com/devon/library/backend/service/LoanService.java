package com.devon.library.backend.service;

import com.devon.library.backend.model.Book;
import com.devon.library.backend.model.Loan;
import com.devon.library.backend.model.Reservation;
import com.devon.library.backend.model.ReservationStatus;
import com.devon.library.backend.repository.BookRepository;
import com.devon.library.backend.repository.LoanRepository;
import com.devon.library.backend.repository.ReservationRepository;
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

  @Inject
  ReservationRepository reservationRepository;

  public Loan checkout(Long userId, Long bookId) {
    Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
    // Block duplicate active loan for the same book by the same user
    boolean alreadyBorrowed = loanRepository.findByUserId(userId).stream()
        .anyMatch(l -> l.getBookId().equals(bookId) && !l.isReturned());
    if (alreadyBorrowed) {
      throw new IllegalStateException("User already has an active loan for this book");
    }
    // Simple borrow limit per user: max 5 active loans
    long activeLoans = loanRepository.findByUserId(userId).stream().filter(l -> !l.isReturned()).count();
    if (activeLoans >= 5) {
      throw new IllegalStateException("Borrow limit reached (5)");
    }
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
      // If there are waiting reservations, make the first AVAILABLE and keep stock reserved for them.
      var waiting = reservationRepository.findByBookId(book.getId()).stream()
          .filter(r -> r.getStatus() == ReservationStatus.WAITING)
          .sorted(java.util.Comparator.comparing(Reservation::getQueuePosition))
          .findFirst();
      if (waiting.isPresent()) {
        Reservation next = waiting.get();
        next.setStatus(ReservationStatus.AVAILABLE);
        next.setExpiryDate(java.time.LocalDate.now().plusDays(7));
        reservationRepository.save(next);
        // Do not increase availableCopies; hold the copy for the reservation claim window
      } else {
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
      }
    });
    return loan;
  }

  public Optional<Loan> getLoan(Long id) {
    return loanRepository.findById(id);
  }

  public List<Loan> loansByUser(Long userId) {
    return loanRepository.findByUserId(userId);
  }

  public List<Loan> findAll() {
    return loanRepository.findAll();
  }

  public Loan renew(Long loanId) {
    Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
    if (loan.isReturned()) {
      return loan;
    }
    Book book = bookRepository.findById(loan.getBookId()).orElseThrow(() -> new IllegalArgumentException("Book not found"));
    // Renewal eligibility: if there is someone waiting (WAITING or AVAILABLE not claimed), deny
    boolean queueExists = reservationRepository.findByBookId(book.getId()).stream()
        .anyMatch(r -> r.getStatus() == ReservationStatus.WAITING || r.getStatus() == ReservationStatus.AVAILABLE);
    if (queueExists) {
      throw new IllegalStateException("Cannot renew: reservation queue exists");
    }
    int loanDays = book.getPageCount() > 300 ? 21 : 14;
    loan.setDueDate(loan.getDueDate().plusDays(loanDays));
    return loanRepository.save(loan);
  }
}


