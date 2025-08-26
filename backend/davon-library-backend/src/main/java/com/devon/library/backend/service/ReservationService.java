package com.devon.library.backend.service;

import com.devon.library.backend.model.Book;
import com.devon.library.backend.model.Reservation;
import com.devon.library.backend.model.ReservationStatus;
import com.devon.library.backend.repository.BookRepository;
import com.devon.library.backend.repository.ReservationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReservationService {

  @Inject
  ReservationRepository reservationRepository;

  @Inject
  BookRepository bookRepository;

  public Reservation createReservation(Long userId, Long bookId) {
    Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
    // Enforce: reserve only if no copies are available
    if (book.getAvailableCopies() > 0) {
      throw new IllegalStateException("Copies are available; please checkout instead of reserving");
    }
    List<Reservation> reservationsForBook = reservationRepository.findByBookId(bookId).stream()
        .filter(r -> r.getStatus() == ReservationStatus.WAITING || r.getStatus() == ReservationStatus.AVAILABLE)
        .sorted(Comparator.comparing(Reservation::getQueuePosition))
        .collect(Collectors.toList());

    boolean alreadyQueued = reservationsForBook.stream().anyMatch(r -> r.getUserId().equals(userId));
    if (alreadyQueued) {
      return reservationsForBook.stream().filter(r -> r.getUserId().equals(userId)).findFirst().get();
    }

    int nextPos = reservationsForBook.size() + 1;
    Reservation reservation = Reservation.builder()
        .userId(userId)
        .bookId(bookId)
        .reservationDate(LocalDate.now())
        .expiryDate(LocalDate.now().plusDays(7))
        .queuePosition(nextPos)
        .status(ReservationStatus.WAITING)
        .build();
    return reservationRepository.save(reservation);
  }

  public Optional<Reservation> getReservation(Long id) {
    return reservationRepository.findById(id);
  }

  public List<Reservation> listByUser(Long userId) {
    return reservationRepository.findByUserId(userId);
  }

  public List<Reservation> listByBook(Long bookId) {
    return reservationRepository.findByBookId(bookId);
  }

  public Reservation cancel(Long reservationId) {
    Reservation r = reservationRepository.findById(reservationId).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    if (r.getStatus() == ReservationStatus.CANCELLED || r.getStatus() == ReservationStatus.COMPLETED || r.getStatus() == ReservationStatus.EXPIRED) {
      return r;
    }
    r.setStatus(ReservationStatus.CANCELLED);
    reservationRepository.save(r);
    // re-number queue positions for WAITING reservations
    List<Reservation> remaining = reservationRepository.findByBookId(r.getBookId()).stream()
        .filter(x -> x.getStatus() == ReservationStatus.WAITING)
        .sorted(Comparator.comparing(Reservation::getQueuePosition))
        .collect(Collectors.toList());
    int pos = 1;
    for (Reservation res : remaining) {
      res.setQueuePosition(pos++);
      reservationRepository.save(res);
    }
    return r;
  }
}


