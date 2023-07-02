package com.db.feedhub.service;

import static com.db.feedhub.data.SessionData.session_valid_1;
import static java.lang.String.valueOf;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

import com.db.feedhub.model.entity.Session;
import com.db.feedhub.model.entity.User;
import com.db.feedhub.repository.SessionRepository;
import com.db.feedhub.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class SessionServiceTest {

  @Autowired
  private SessionService sessionService;

  @Autowired
  private SessionRepository sessionRepository;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    sessionRepository.saveAll(Stream.of(new Session(), new Session(), new Session()).toList());
  }

  @AfterEach
  void clean() {
    sessionRepository.deleteAll();
  }

  @Test
  void save_successful() {
    long countBeforeSave = sessionRepository.count();

    Session session = sessionService.save(session_valid_1());

    long countAfterSave = sessionRepository.count();

    assertThat(session).isNotNull();
    assertThat(session.getId()).isNotNull();
    assertThat(session.getStartDateTime()).isNotNull();
    assertThat(session.getEndDateTime()).isNotNull();
    assertThat(session.getEndDateTime()).isAfter(session.getStartDateTime());
    assertThat(countAfterSave).isEqualTo(countBeforeSave + 1);
  }

  @Test
  void saveAll_successful() {
    long countBeforeSave = sessionRepository.count();

    List<Session> sessions = sessionService.saveAll(of(new Session(), new Session()));

    long countAfterSave = sessionRepository.count();

    assertThat(sessions).isNotNull();
    assertThat(sessions).isNotEmpty();
    assertThat(countAfterSave).isEqualTo(countBeforeSave + 2);
  }

  @Test
  void findAll_successful() {
    Page<Session> page = sessionService.findAll(Pageable.ofSize(4));
    assertThat(page).isNotNull();
    assertThat(page.getTotalElements()).isEqualTo(3);
    assertThat(page.getTotalPages()).isEqualTo(1);
  }

  @Test
  void isValidSession_successful() {
    Session session = sessionRepository.save(new Session());
    assertThat(sessionService.isValidSession(session.getId())).isTrue();
  }

  @Test
  void isValidSession_failed_noSessionFound() {
    assertThat(sessionService.isValidSession(UUID.randomUUID())).isFalse();
  }

  @Test
  void isValidSession_failed_invalidStartDate() {
    Session session = sessionRepository.save(Session.builder()
        .startDateTime(LocalDateTime.now().plusHours(1))
        .build());
    assertThat(sessionService.isValidSession(session.getId())).isFalse();
  }

  @Test
  void isValidSession_failed_invalidEndDate() {
    Session session = sessionRepository.save(Session.builder()
        .startDateTime(LocalDateTime.now().minusHours(9))
        .endDateTime(LocalDateTime.now().minusHours(1))
        .build());
    assertThat(sessionService.isValidSession(session.getId())).isFalse();
  }

  @Test
  void delete_successful() {
    long countBeforeSave = sessionRepository.count();

    Session session = sessionRepository.save(new Session());

    long countAfterSave = sessionRepository.count();

    assertThat(countAfterSave).isEqualTo(countBeforeSave + 1);

    sessionService.delete(session.getId());

    Optional<Session> deletedSession = sessionRepository.findById(session.getId());

    assertThat(deletedSession).isEmpty();
    assertThat(sessionRepository.count()).isEqualTo(countBeforeSave);
  }

  @Test
  void generateSessions_successful() {
    sessionRepository.deleteAll();
    userRepository.saveAll(of(User.builder()
            .email("test1@company.com")
            .build(),
        User.builder()
            .email("test2@company.com")
            .build()));

    sessionService.generateSessions();

    assertThat(sessionRepository.count()).isEqualTo(userRepository.count());
  }

  @Test
  void generateSessions_successful_for200Users() {
    sessionRepository.deleteAll();

    List<User> users = new ArrayList<>();
    for (int i = 0; i < 200; i++) {
      users.add(User.builder().email(valueOf(i).concat("@company.com")).build());
    }
    userRepository.saveAll(users);

    sessionService.generateSessions();

    assertThat(sessionRepository.count()).isEqualTo(userRepository.count());
  }

  @Test
  void generateSessions_successful_withUnsubscribedUser() {
    sessionRepository.deleteAll();
    userRepository.saveAll(of(User.builder()
            .email("test1@company.com")
            .build(),
        User.builder()
            .email("test2@company.com")
            .unsubscribe(true)
            .unsubscribeDateTime(LocalDateTime.now())
            .build()));

    sessionService.generateSessions();

    assertThat(sessionRepository.count()).isEqualTo(1);
  }

  @Test
  void generateSessions_failed_alreadyGenerated() {
    sessionRepository.deleteAll();
    userRepository.saveAll(of(User.builder()
            .email("test1@company.com")
            .build(),
        User.builder()
            .email("test2@company.com")
            .build()));

    sessionService.generateSessions();

    List<Session> sessions = sessionRepository.findAll();

    sessionService.generateSessions();

    assertThat(sessionRepository.findAll()).containsExactlyInAnyOrderElementsOf(sessions);
  }
}
