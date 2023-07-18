package com.db.feedhub.service;

import static com.db.feedhub.data.SessionData.session_valid_1;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import com.db.feedhub.model.entity.Feedback;
import com.db.feedhub.model.entity.Session;
import com.db.feedhub.repository.FeedbackRepository;
import com.db.feedhub.repository.SessionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    sessionRepository.deleteAll();
  }

  @Test
  void findById_successful() {
    Session session = sessionRepository.save(session_valid_1());

    Feedback feedback = Feedback.builder()
        .sessionId(session.getId())
        .dateTime(now())
        .comment("Good !")
        .note(5)
        .build();

    Feedback savedFeedback = feedbackService.save(feedback);

    Optional<Feedback> result = feedbackService.findById(savedFeedback.getId());
    assertThat(result).isPresent();
    assertThat(result.get().getComment()).isEqualTo(feedback.getComment());
    assertThat(result.get().getSessionId().toString()).isEqualTo(
        feedback.getSessionId().toString());
    assertThat(result.get().getNote()).isEqualTo(feedback.getNote());
    assertThat(result.get().isCensored()).isFalse();
  }

  @Test
  void findById_failed_notFound() {
    Optional<Feedback> result = feedbackService.findById(randomUUID());
    assertThat(result).isEmpty();
  }

  @Test
  void findByDateTimeBetween_successful() {
    Session s1 = sessionRepository.save(new Session());
    Feedback f1 = Feedback.builder()
        .id(randomUUID())
        .sessionId(s1.getId())
        .note(5)
        .dateTime(LocalDateTime.now())
        .build();
    Session s2 = sessionRepository.save(new Session());
    Feedback f2 = Feedback.builder()
        .id(randomUUID())
        .sessionId(s2.getId())
        .note(5)
        .dateTime(LocalDateTime.now())
        .build();
    Session s3 = sessionRepository.save(new Session());
    Feedback f3 = Feedback.builder()
        .id(randomUUID())
        .sessionId(s3.getId())
        .note(10)
        .dateTime(LocalDateTime.of(LocalDate.of(2023, 1, 1), LocalTime.MIN))
        .build();

    feedbackRepository.saveAll(Stream.of(f1, f2, f3).toList());

    List<Feedback> feedbacks = feedbackRepository.findAll();
    assertThat(feedbacks).isNotEmpty();

    List<Feedback> result = feedbackService.findAllByDateBetween(LocalDate.now(), LocalDate.now());

    assertThat(result).isNotEmpty();
    assertThat(result).hasSize(2);
    assertThat(result).containsExactlyInAnyOrderElementsOf(
        feedbacks.stream().filter(f -> f.getNote() == 5).toList());
  }

  @Test
  void findPageByDateTimeBetween_successful() {
    Session s1 = sessionRepository.save(new Session());
    Feedback f1 = Feedback.builder()
        .id(randomUUID())
        .sessionId(s1.getId())
        .note(5)
        .dateTime(LocalDateTime.now())
        .build();
    Session s2 = sessionRepository.save(new Session());
    Feedback f2 = Feedback.builder()
        .id(randomUUID())
        .sessionId(s2.getId())
        .note(5)
        .dateTime(LocalDateTime.now())
        .build();
    Session s3 = sessionRepository.save(new Session());
    Feedback f3 = Feedback.builder()
        .id(randomUUID())
        .sessionId(s3.getId())
        .note(10)
        .dateTime(LocalDateTime.of(LocalDate.of(2023, 1, 1), LocalTime.MIN))
        .build();

    feedbackRepository.saveAll(Stream.of(f1, f2, f3).toList());

    List<Feedback> feedbacks = feedbackRepository.findAll();
    assertThat(feedbacks).isNotEmpty();

    Page<Feedback> result = feedbackService.findPageByDateBetween(LocalDate.now(),
        LocalDate.now(),
        Pageable.ofSize(5));

    assertThat(result).isNotNull();
    assertThat(result.getTotalPages()).isEqualTo(1);
    assertThat(result.getContent())
        .containsExactlyInAnyOrderElementsOf(feedbacks.stream()
            .filter(f -> f.getNote() == 5)
            .toList());
  }

  @Test
  void findByDateTimeBetween_failed() {
    Exception exception = null;

    try {
      feedbackService.findAllByDateBetween(LocalDate.now(), LocalDate.now().minusDays(1));
    } catch (IllegalArgumentException argumentException) {
      exception = argumentException;
    }

    assertThat(exception).isNotNull();
  }

}
