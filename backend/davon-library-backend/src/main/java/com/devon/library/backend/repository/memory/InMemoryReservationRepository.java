package com.devon.library.backend.repository.memory;

import com.devon.library.backend.model.Reservation;
import com.devon.library.backend.repository.ReservationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryReservationRepository implements ReservationRepository {

  private final Map<Long, Reservation> idToReservation = new ConcurrentHashMap<>();
  private final AtomicLong idSequence = new AtomicLong(1);

  @Override
  public Reservation save(Reservation reservation) {
    if (reservation.getId() == null) {
      reservation.setId(idSequence.getAndIncrement());
    }
    idToReservation.put(reservation.getId(), reservation);
    return reservation;
  }

  @Override
  public Optional<Reservation> findById(Long id) {
    return Optional.ofNullable(idToReservation.get(id));
  }

  @Override
  public List<Reservation> findByUserId(Long userId) {
    return idToReservation.values().stream()
        .filter(r -> r.getUserId().equals(userId))
        .collect(Collectors.toList());
  }

  @Override
  public List<Reservation> findByBookId(Long bookId) {
    return idToReservation.values().stream()
        .filter(r -> r.getBookId().equals(bookId))
        .collect(Collectors.toList());
  }

  @Override
  public List<Reservation> findAll() {
    return new ArrayList<>(idToReservation.values());
  }

  @Override
  public void deleteById(Long id) {
    idToReservation.remove(id);
  }
}


