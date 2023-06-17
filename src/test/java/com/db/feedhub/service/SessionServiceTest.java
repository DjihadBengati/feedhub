package com.db.feedhub.service;

import static com.db.feedhub.data.SessionData.session_valid_1;
import static org.assertj.core.api.Assertions.assertThat;

import com.db.feedhub.model.entity.Session;
import com.db.feedhub.repository.SessionRepository;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SessionServiceTest {

  @Autowired
  private SessionService sessionService;

  @Autowired
  private SessionRepository sessionRepository;

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
    Session session = sessionService.save(session_valid_1());
    assertThat(session).isNotNull();
    assertThat(session.getId()).isNotNull();
    assertThat(session.getStartDateTime()).isNotNull();
    assertThat(session.getEndDateTime()).isNotNull();
    assertThat(session.getEndDateTime()).isAfter(session.getStartDateTime());
  }
}
