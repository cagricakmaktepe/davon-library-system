package com.devon.library.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import com.devon.library.backend.model.Book;
import com.devon.library.backend.repository.memory.InMemoryBookRepository;
import com.devon.library.backend.repository.memory.InMemoryLoanRepository;
import com.devon.library.backend.repository.memory.InMemoryReservationRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoanServiceTest {

  private LoanService loanService;
  private InMemoryBookRepository bookRepository;

  @BeforeEach
  void setup() {
    loanService = new LoanService();
    InMemoryLoanRepository loanRepository = new InMemoryLoanRepository();
    bookRepository = new InMemoryBookRepository();
    var reservationRepository = new InMemoryReservationRepository();

    // Manually inject (no CDI in unit test)
    loanService.loanRepository = loanRepository;
    loanService.bookRepository = bookRepository;
    loanService.reservationRepository = reservationRepository;

    // Seed a book
    Book book = Book.builder().title("Test").isbn("X").pageCount(200).totalCopies(2).availableCopies(2).build();
    bookRepository.save(book);
  }

  @Test
  void checkoutAndReturn_shouldUpdateInventory() {
    Long userId = 1L;
    Long bookId = bookRepository.findAll().get(0).getId();

    var loan = loanService.checkout(userId, bookId);
    assertNotNull(loan.getId());
    assertEquals(LocalDate.now().plusDays(14), loan.getDueDate());

    var book = bookRepository.findById(bookId).orElseThrow();
    assertEquals(1, book.getAvailableCopies());

    var returned = loanService.returnBook(loan.getId());
    assertNotNull(returned.getReturnDate());
    var after = bookRepository.findById(bookId).orElseThrow();
    assertEquals(2, after.getAvailableCopies());
  }

  @Test
  void checkout_shouldUse21DaysForLargeBooks() {
    Book big = Book.builder().title("Big").isbn("Y").pageCount(500).totalCopies(1).availableCopies(1).build();
    bookRepository.save(big);
    var loan = loanService.checkout(2L, big.getId());
    assertEquals(LocalDate.now().plusDays(21), loan.getDueDate());
  }
}


