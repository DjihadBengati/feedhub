package com.db.feedhub.model.service;

import com.db.feedhub.model.entity.Feedback;
import com.db.feedhub.repository.FeedbackRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FeedbackService {

  private FeedbackRepository feedbackRepository;

  public FeedbackService(FeedbackRepository feedbackRepository) {
    this.feedbackRepository = feedbackRepository;
  }

  public Feedback save(Feedback feedback) {
    log.debug("Saving new feedback: {}", feedback);
    // TODO check session validity before saving
    return feedbackRepository.save(feedback);
  }

  public Optional<Feedback> findById(UUID id) {
    log.debug("Getting feedback with id: {}", id);
    return feedbackRepository.findById(id);
  }

  public Page<Feedback> findAll(Pageable pageable) {
    log.debug("Getting feedback list");
    return feedbackRepository.findAll(pageable);
  }
}
