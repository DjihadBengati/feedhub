package com.db.feedhub.resource;

import static com.db.feedhub.mapper.FeedbackMapper.map;

import com.db.feedhub.model.api.FeedbackApi;
import com.db.feedhub.model.entity.Feedback;
import com.db.feedhub.service.FeedbackService;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/feedback")
@Slf4j
public class FeedbackResource {

  private FeedbackService feedbackService;

  public FeedbackResource(FeedbackService feedbackService) {
    this.feedbackService = feedbackService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<FeedbackApi> findById(@PathVariable UUID id) {
    Optional<Feedback> feedback = feedbackService.findById(id);
    return feedback.map(value -> ResponseEntity.ok(map(value)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // TODO test resource
  @GetMapping
  public Page<Feedback> findAll(Pageable pageable) {
    return feedbackService.findAll(pageable);
  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody FeedbackApi feedbackApi,
      UriComponentsBuilder uriBuilder) {
    try {
      Feedback feedback = feedbackService.save(map(feedbackApi));
      String resourceUri = uriBuilder.path("/api/v1/feedback/{id}")
          .buildAndExpand(feedback.getId())
          .toUriString();
      return ResponseEntity.created(UriComponentsBuilder
              .fromUriString(resourceUri)
              .build().toUri())
          .build();
    } catch (IllegalArgumentException exception) {
      log.error("Error creating a feedback", exception);
      return ResponseEntity.badRequest().build();
    }
  }
}
