package com.db.feedhub.service;

import static java.util.Objects.isNull;

import com.db.feedhub.model.entity.Feedback;
import com.db.feedhub.repository.FeedbackRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FeedbackService {

  private final FeedbackRepository feedbackRepository;

  private final SessionService sessionService;

  public FeedbackService(FeedbackRepository feedbackRepository, SessionService sessionService) {
    this.feedbackRepository = feedbackRepository;
    this.sessionService = sessionService;
  }

  public Feedback save(Feedback feedback) {
    log.debug("Saving new feedback: {}", feedback);

    if (isNull(feedback.getSessionId()) ||
        !sessionService.isValidSession(feedback.getSessionId().toString())) {
      log.error("Invalid session ID for feedback: {}", feedback);
      throw new IllegalArgumentException("Invalid session ID");
    }

    feedback.setDateTime(LocalDateTime.now());
    Feedback savedFeedback = feedbackRepository.save(feedback);

    sessionService.delete(savedFeedback.getSessionId().toString());

    return savedFeedback;
  }

  public Optional<Feedback> findById(UUID id) {
    log.debug("Getting feedback with id: {}", id);
    return feedbackRepository.findById(id);
  }

  // TODO test this function
  public Page<Feedback> findAll(Pageable pageable) {
    log.debug("Getting feedback list");
    return feedbackRepository.findAll(pageable);
  }
}
