package com.db.feedhub.resource;

import static com.db.feedhub.mapper.FeedbackMapper.map;
import static java.util.Objects.requireNonNull;

import com.db.feedhub.model.api.FeedbackApi;
import com.db.feedhub.model.entity.Feedback;
import com.db.feedhub.model.service.FeedbackService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
public class FeedbackResource {

  private FeedbackService feedbackService;

  public FeedbackResource(FeedbackService feedbackService) {
    this.feedbackService = feedbackService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<FeedbackApi> findById(@PathVariable UUID id) {
    Optional<Feedback> feedback = feedbackService.findById(id);
    // TODO if the feedback date equals now do not send the feedback
    return feedback.map(value -> ResponseEntity.ok(map(value)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public Page<Feedback> findAll(Pageable pageable) {
    // TODO do not send today's feedbacks
    return null;
  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody FeedbackApi feedbackApi,
      UriComponentsBuilder uriBuilder) {
    Feedback feedback = feedbackService.save(map(feedbackApi));
    String resourceUri = uriBuilder.path("/api/v1/feedback/{id}")
        .buildAndExpand(feedback.getId())
        .toUriString();
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(UriComponentsBuilder.fromUriString(resourceUri).build().toUri());
    return ResponseEntity.created(requireNonNull(headers.getLocation())).build();
  }
}
