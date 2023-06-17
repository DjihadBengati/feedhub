package com.db.feedhub.service;

import static com.db.feedhub.data.UserData.user_subscribed;
import static com.db.feedhub.data.UserData.user_unsubscribed;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

import com.db.feedhub.model.entity.User;
import com.db.feedhub.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  UserService userService;

  @BeforeEach
  void setUp() {
    userRepository.saveAll(of(user_subscribed(), user_unsubscribed()));
  }

  @AfterEach
  void clean() {
    userRepository.deleteAll();
  }

  @Test
  void save_successful() {
    User userToSave = new User();
    userToSave.setEmail("user1@company.com");

    User savedUser = userService.save(userToSave);
    assertThat(savedUser).isNotNull();
    assertThat(savedUser.getEmail()).isEqualTo(userToSave.getEmail());
    assertThat(savedUser.getUnsubscribeDateTime()).isNull();
    assertThat(savedUser.isUnsubscribe()).isFalse();
  }

  @Test
  void save_failed_emailAlreadyUsed() {
    Exception exception = null;

    try {
      userService.save(user_subscribed());
    } catch (Exception e) {
      exception = e;
    }

    assertThat(exception).isNotNull();
  }

  @Test
  void save_failed_invalidEmail() {
    Exception exception = null;
    User userToSave = new User();
    userToSave.setEmail("test-email.com");

    try {
      userService.save(userToSave);
    } catch (Exception e) {
      exception = e;
    }

    assertThat(exception).isNotNull();
  }

  @Test
  void save_failed_invalidEmailCompanyPattern() {
    Exception exception = null;
    User userToSave = new User();
    userToSave.setEmail("test@email.com");

    try {
      userService.save(userToSave);
    } catch (Exception e) {
      exception = e;
    }

    assertThat(exception).isNotNull();
  }
}
