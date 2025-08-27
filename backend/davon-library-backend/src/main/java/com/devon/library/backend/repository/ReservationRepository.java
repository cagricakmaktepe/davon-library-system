package com.devon.library.backend.repository;

import com.devon.library.backend.model.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
  Reservation save(Reservation reservation);
  Optional<Reservation> findById(Long id);
  List<Reservation> findByUserId(Long userId);
  List<Reservation> findByBookId(Long bookId);
  List<Reservation> findAll();
  void deleteById(Long id);
}


