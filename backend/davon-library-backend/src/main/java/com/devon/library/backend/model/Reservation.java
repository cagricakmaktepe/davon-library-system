package com.devon.library.backend.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
  private Long id;
  private Long userId;
  private Long bookId;
  private LocalDate reservationDate;
  private LocalDate expiryDate;
  private Integer queuePosition;
  private ReservationStatus status;
}


