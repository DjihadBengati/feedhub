package com.db.feedhub.service;

import static com.db.feedhub.data.UserData.user_subscribed;
import static com.db.feedhub.data.UserData.user_unsubscribed;
import static java.time.LocalDateTime.now;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

import com.db.feedhub.model.entity.User;
import com.db.feedhub.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

  @Test
  void findAll_successful() {
    Page<User> page = userService.findAll(Pageable.ofSize(10));
    assertThat(page).isNotNull();
    assertThat(page.getTotalPages()).isOne();
    assertThat(page.getContent().size()).isEqualTo(2);
    assertThat(page.hasNext()).isFalse();
  }

  @Test
  void save_failed_emailPatternNotValidForEntity() {
    User userToSave = new User();
    userToSave.setEmail("test");
    Exception catchedException = null;

    try {
      userRepository.save(userToSave);
    } catch (Exception exception) {
      catchedException = exception;
    }

    assertThat(catchedException).isNotNull();
  }

  @Test
  void saveAll_successful() {
    long countBefore = userRepository.count();

    User user1 = new User();
    user1.setEmail("saveAll1@company.com");
    User user2 = new User();
    user2.setEmail("saveAll2@company.com");

    userService.saveAll(of(user1, user2));

    assertThat(userRepository.count()).isEqualTo(countBefore + 2);
  }

  @Test
  void saveAll_failed_oneUserWithWrongCompanyEmailPattern() {
    User user1 = new User();
    user1.setEmail("saveAll1@company.com");
    User user2 = new User();
    user2.setEmail("saveAll2@t.com");

    Exception catchedException = null;

    try {
      userService.saveAll(of(user2, user1));
    } catch (Exception exception) {
      catchedException = exception;
    }

    assertThat(catchedException).isNotNull();
  }

  @Test
  void delete_successful() {
    User user = new User();
    user.setEmail("delete@company.com");

    User savedUser = userRepository.save(user);
    long countAfterSave = userRepository.count();

    userService.delete(savedUser.getId().toString());
    long countAfterDelete = userRepository.count();

    assertThat(countAfterDelete).isEqualTo(countAfterSave - 1);
  }

  @Test
  void deleteByEmail_successful() {
    User user = new User();
    user.setEmail("delete@company.com");

    User savedUser = userRepository.save(user);
    long countAfterSave = userRepository.count();

    userService.deleteByEmail(savedUser.getEmail());
    long countAfterDelete = userRepository.count();

    assertThat(countAfterDelete).isEqualTo(countAfterSave - 1);
  }

  @Test
  void unsubscribe_successful() {
    LocalDateTime now = now();
    User userUnsubscribed = userService.unsubscribe(user_subscribed().getEmail());
    assertThat(userUnsubscribed.isUnsubscribe()).isTrue();
    assertThat(userUnsubscribed.getUnsubscribeDateTime()).isNotNull();
    assertThat(userUnsubscribed.getUnsubscribeDateTime().getYear()).isEqualTo(now.getYear());
    assertThat(userUnsubscribed.getUnsubscribeDateTime().getMonth()).isEqualTo(now.getMonth());
    assertThat(userUnsubscribed.getUnsubscribeDateTime().getDayOfMonth()).isEqualTo(
        now.getDayOfMonth());
    assertThat(userUnsubscribed.getUnsubscribeDateTime().getHour()).isEqualTo(now.getHour());
    assertThat(userUnsubscribed.getUnsubscribeDateTime().getMinute()).isEqualTo(now.getMinute());
  }

  @Test
  void subscribe_successful() {
    User userSubscribed = userService.subscribe(user_unsubscribed().getEmail());
    assertThat(userSubscribed.isUnsubscribe()).isFalse();
    assertThat(userSubscribed.getUnsubscribeDateTime()).isNull();
  }

  @Test
  void unsubscribe_failed_alreadyUnsubscribed() {
    Exception catchedException = null;
    try {
      userService.unsubscribe(user_unsubscribed().getEmail());
    } catch (IllegalArgumentException exception) {
      catchedException = exception;
    }
    assertThat(catchedException).isNotNull();
  }

  @Test
  void subscribe_failed_alreadySubscribed() {
    Exception catchedException = null;
    try {
      userService.subscribe(user_subscribed().getEmail());
    } catch (IllegalArgumentException exception) {
      catchedException = exception;
    }
    assertThat(catchedException).isNotNull();
  }

  @Test
  void unsubscribe_failed_unknownEmail() {
    Exception catchedException = null;
    try {
      userService.unsubscribe("unknown@company.com");
    } catch (IllegalArgumentException exception) {
      catchedException = exception;
    }
    assertThat(catchedException).isNotNull();
  }

  @Test
  void subscribe_failed_unknownEmail() {
    Exception catchedException = null;
    try {
      userService.subscribe("unknown@company.com");
    } catch (IllegalArgumentException exception) {
      catchedException = exception;
    }
    assertThat(catchedException).isNotNull();
  }
}
