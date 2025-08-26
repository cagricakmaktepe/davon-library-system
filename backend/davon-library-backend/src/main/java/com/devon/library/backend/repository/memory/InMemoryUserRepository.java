package com.devon.library.backend.repository.memory;

import com.devon.library.backend.model.User;
import com.devon.library.backend.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class InMemoryUserRepository implements UserRepository {
  private final Map<Long, User> idToUser = new ConcurrentHashMap<>();
  private final AtomicLong idSequence = new AtomicLong(1);

  @Override
  public User save(User user) {
    if (user.getId() == null) {
      user.setId(idSequence.getAndIncrement());
    }
    idToUser.put(user.getId(), user);
    return user;
  }

  @Override
  public Optional<User> findById(Long id) {
    return Optional.ofNullable(idToUser.get(id));
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return idToUser.values().stream().filter(u -> email != null && email.equalsIgnoreCase(u.getEmail())).findFirst();
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<>(idToUser.values());
  }

  @Override
  public void deleteById(Long id) {
    idToUser.remove(id);
  }
}


