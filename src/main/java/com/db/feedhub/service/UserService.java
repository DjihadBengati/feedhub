package com.db.feedhub.service;

import static com.db.feedhub.util.CompanyUtils.isValidEmail;

import com.db.feedhub.config.CompanyConfig;
import com.db.feedhub.model.entity.User;
import com.db.feedhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  private final CompanyConfig companyConfig;

  public Optional<User> findById(@NonNull UUID id) {
    log.debug("Getting user with id: {}", id);
    return userRepository.findById(id);
  }

  public User save(@NonNull User user) {
    log.debug("Saving new user: {}", user);
    checkCompanyEmailPattern(user);
    return userRepository.save(user);
  }

  public List<User> saveAll(@NonNull List<User> users) {
    log.debug("Saving {} user(s)", users.size());
    users.forEach(this::checkCompanyEmailPattern);
    return userRepository.saveAll(users);
  }

  public void delete(@NonNull UUID id) {
    log.debug("Deleting user with ID: {}", id);
    userRepository.deleteById(id);
  }

  @Transactional
  public void deleteByEmail(@NonNull String email) {
    log.debug("Deleting user with email: {}", email);
    userRepository.deleteByEmail(email);
  }

  public User unsubscribe(@NonNull String email) {
    log.debug("Unsubscribe user with email: {}", email);
    Optional<User> user = userRepository.findByEmail(email);

    if (user.isPresent() && !user.get().isUnsubscribe()) {
      User userToUpdate = user.get();
      userToUpdate.setUnsubscribe(true);
      userToUpdate.setUnsubscribeDateTime(LocalDateTime.now());
      return userRepository.save(userToUpdate);
    }

    throw new IllegalArgumentException("User not found or already unsubscribed");
  }

  // TODO test this function
  public User subscribe(@NonNull String email) {
    log.debug("Subscribing user with email: {}", email);
    Optional<User> user = userRepository.findByEmail(email);

    if (user.isEmpty() || !user.get().isUnsubscribe()) {
      throw new IllegalArgumentException("User not found or already subscribed");
    }

    User userToUpdate = user.get();
    userToUpdate.setUnsubscribe(false);
    userToUpdate.setUnsubscribeDateTime(null);
    return userRepository.save(userToUpdate);
  }

  public Page<User> findAll(@NonNull Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  private void checkCompanyEmailPattern(@NonNull User user) {
    if (!isValidEmail(user.getEmail(), companyConfig.getEmailPattern())) {
      log.error("Invalid email: {}", user);
      throw new IllegalArgumentException("Invalid email");
    }
  }
}
