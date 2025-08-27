package com.devon.library.backend.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class ReservationTest {

  @Test
  void builder_shouldSetFields() {
    Reservation r = Reservation.builder()
        .id(1L)
        .userId(10L)
        .bookId(20L)
        .reservationDate(LocalDate.now())
        .expiryDate(LocalDate.now().plusDays(7))
        .queuePosition(1)
        .status(ReservationStatus.WAITING)
        .build();
    assertEquals(10L, r.getUserId());
    assertEquals(20L, r.getBookId());
    assertEquals(1, r.getQueuePosition());
    assertEquals(ReservationStatus.WAITING, r.getStatus());
  }
}



