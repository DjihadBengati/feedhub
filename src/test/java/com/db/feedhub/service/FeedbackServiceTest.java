package com.db.feedhub.service;

import static com.db.feedhub.data.SessionData.session_valid_1;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import com.db.feedhub.model.entity.Feedback;
import com.db.feedhub.model.entity.Session;
import com.db.feedhub.repository.FeedbackRepository;
import com.db.feedhub.repository.SessionRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FeedbackServiceTest {

  @Autowired
  private SessionRepository sessionRepository;

  @Autowired
  private FeedbackRepository feedbackRepository;

  @Autowired
  private FeedbackService feedbackService;

  @AfterEach
  void clean() {
    feedbackRepository.deleteAll();
  }

  @Test
  void findById_successful() {
    Session session = sessionRepository.save(session_valid_1());

    Feedback feedback = new Feedback();
    feedback.setSessionId(session.getId());
    feedback.setDateTime(now());
    feedback.setComment("Good !");
    feedback.setNote(5);

    Feedback savedFeedback = feedbackService.save(feedback);

    Optional<Feedback> result = feedbackService.findById(savedFeedback.getId());
    assertThat(result).isPresent();
    assertThat(result.get().getComment()).isEqualTo(feedback.getComment());
    assertThat(result.get().getSessionId().toString()).isEqualTo(
        feedback.getSessionId().toString());
    assertThat(result.get().getNote()).isEqualTo(feedback.getNote());
  }

  @Test
  void findById_failed_notFound() {
    Optional<Feedback> result = feedbackService.findById(randomUUID());
    assertThat(result).isEmpty();
  }

}
