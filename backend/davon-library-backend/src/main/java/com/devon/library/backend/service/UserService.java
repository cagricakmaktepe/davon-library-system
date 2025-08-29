package com.devon.library.backend.service;

import com.devon.library.backend.model.User;
import com.devon.library.backend.repository.UserRepository;
import com.devon.library.backend.model.Role;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {

  @Inject
  UserRepository userRepository;

  public User createUser(String name, String email) {
    return createUser(name, email, Role.MEMBER);
  }

  public User createUser(String name, String email, Role role) {
    User user = User.builder().name(name).email(email).role(role == null ? Role.MEMBER : role).build();
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


