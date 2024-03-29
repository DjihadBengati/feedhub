package com.db.feedhub.service;

import static java.util.Objects.isNull;

import com.db.feedhub.model.entity.Feedback;
import com.db.feedhub.repository.FeedbackRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class FeedbackService {

  private final FeedbackRepository feedbackRepository;

  private final SessionService sessionService;

  public Feedback save(@NonNull Feedback feedback) {
    log.debug("Saving new feedback: {}", feedback);

    if (isNull(feedback.getSessionId()) ||
        !sessionService.isValidSession(feedback.getSessionId())) {
      log.error("Invalid session ID for feedback: {}", feedback);
      throw new IllegalArgumentException("Invalid session ID");
    }

    if (feedback.getNote() > 10 || feedback.getNote() < 0) {
      log.error("Invalid note for feedback: {}", feedback);
      throw new IllegalArgumentException("Invalid note");
    }

    feedback.setDateTime(LocalDateTime.now());
    Feedback savedFeedback = feedbackRepository.save(feedback);

    sessionService.delete(savedFeedback.getSessionId());

    return savedFeedback;
  }

  // TODO test this function
  public Feedback update(@NonNull Feedback feedback) {
    log.debug("Updating feedback: {}", feedback);
    return feedbackRepository.save(feedback);
  }

  public Optional<Feedback> findById(@NonNull UUID id) {
    log.debug("Getting feedback with id: {}", id);
    return feedbackRepository.findById(id);
  }

  // TODO test this function
  public Page<Feedback> findAll(@NonNull Pageable pageable) {
    log.debug("Getting feedbacks list");
    return feedbackRepository.findAll(pageable);
  }

  // TODO test this function
  public void delete(@NonNull UUID id) {
    log.debug("Deleting feedback list");
    feedbackRepository.deleteById(id);
  }

  public Page<Feedback> findAllCensored(Pageable pageable) {
    log.debug("Getting censored feedbacks list");
    return feedbackRepository.findByCensoredTrue(pageable);
  }

  @Transactional
  public List<Feedback> findAllByDateBetween(@NonNull LocalDate start, @NonNull LocalDate end) {
    log.debug("Getting feedbacks between {} and {}", start, end);
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Start date is after end date");
    }

    return feedbackRepository.findAllByDateTimeBetween(start.atStartOfDay(), end.atTime(LocalTime.MAX));
  }
}
