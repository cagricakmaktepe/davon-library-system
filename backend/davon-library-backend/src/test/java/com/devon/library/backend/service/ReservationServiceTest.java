package com.devon.library.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import com.devon.library.backend.model.Book;
import com.devon.library.backend.model.Reservation;
import com.devon.library.backend.model.ReservationStatus;
import com.devon.library.backend.repository.memory.InMemoryBookRepository;
import com.devon.library.backend.repository.memory.InMemoryReservationRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationServiceTest {

  private ReservationService reservationService;
  private InMemoryBookRepository bookRepository;

  @BeforeEach
  void setup() {
    reservationService = new ReservationService();
    bookRepository = new InMemoryBookRepository();
    var reservationRepository = new InMemoryReservationRepository();

    reservationService.bookRepository = bookRepository;
    reservationService.reservationRepository = reservationRepository;

    // Seed a book with zero available copies
    Book book = Book.builder().title("Unavailable Book").isbn("R1").pageCount(100).totalCopies(1).availableCopies(0).build();
    bookRepository.save(book);
  }

  @Test
  void createReservation_shouldQueueWhenNoCopies() {
    Long userId = 1L;
    Long bookId = bookRepository.findAll().get(0).getId();
    Reservation r = reservationService.createReservation(userId, bookId);
    assertNotNull(r.getId());
    assertEquals(ReservationStatus.WAITING, r.getStatus());
    assertEquals(1, r.getQueuePosition());
  }

  @Test
  void createReservation_shouldReturnExistingIfAlreadyQueued() {
    Long userId = 2L;
    Long bookId = bookRepository.findAll().get(0).getId();
    var first = reservationService.createReservation(userId, bookId);
    var again = reservationService.createReservation(userId, bookId);
    assertEquals(first.getId(), again.getId());
  }

  @Test
  void cancel_shouldRenumberQueue() {
    Long bookId = bookRepository.findAll().get(0).getId();
    Reservation r1 = reservationService.createReservation(10L, bookId);
    Reservation r2 = reservationService.createReservation(11L, bookId);
    Reservation r3 = reservationService.createReservation(12L, bookId);
    assertEquals(1, r1.getQueuePosition());
    assertEquals(2, r2.getQueuePosition());
    assertEquals(3, r3.getQueuePosition());

    reservationService.cancel(r2.getId());
    List<Reservation> remaining = reservationService.listByBook(bookId);
    var r1after = remaining.stream().filter(r -> r.getUserId().equals(10L)).findFirst().orElseThrow();
    var r3after = remaining.stream().filter(r -> r.getUserId().equals(12L)).findFirst().orElseThrow();
    assertEquals(1, r1after.getQueuePosition());
    assertEquals(2, r3after.getQueuePosition());
  }
}




