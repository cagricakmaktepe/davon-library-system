package com.devon.library.backend.service;

import com.devon.library.backend.model.User;
import com.devon.library.backend.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {

  @Inject
  UserRepository userRepository;

  public User createUser(String name, String email) {
    User user = User.builder().name(name).email(email).build();
    return userRepository.save(user);
  }

  public Optional<User> getUser(Long id) {
    return userRepository.findById(id);
  }

  public List<User> listUsers() {
    return userRepository.findAll();
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}


