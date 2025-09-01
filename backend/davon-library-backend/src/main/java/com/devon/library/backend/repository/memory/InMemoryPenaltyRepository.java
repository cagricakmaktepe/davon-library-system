package com.devon.library.backend.repository.memory;

import com.devon.library.backend.model.Penalty;
import com.devon.library.backend.repository.PenaltyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryPenaltyRepository implements PenaltyRepository {

  private final Map<Long, Penalty> idToPenalty = new ConcurrentHashMap<>();
  private final AtomicLong idSequence = new AtomicLong(1);

  @Override
  public Penalty save(Penalty penalty) {
    if (penalty.getId() == null) {
      penalty.setId(idSequence.getAndIncrement());
    }
    idToPenalty.put(penalty.getId(), penalty);
    return penalty;
  }

  @Override
  public Optional<Penalty> findById(Long id) {
    return Optional.ofNullable(idToPenalty.get(id));
  }

  @Override
  public List<Penalty> findByUserId(Long userId) {
    return idToPenalty.values().stream().filter(p -> p.getUserId().equals(userId)).collect(Collectors.toList());
  }

  @Override
  public List<Penalty> findByLoanId(Long loanId) {
    return idToPenalty.values().stream().filter(p -> p.getLoanId().equals(loanId)).collect(Collectors.toList());
  }

  @Override
  public List<Penalty> findAll() {
    return new ArrayList<>(idToPenalty.values());
  }

  @Override
  public void deleteById(Long id) {
    idToPenalty.remove(id);
  }
}


