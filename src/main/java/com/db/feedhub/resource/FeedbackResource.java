package com.db.feedhub.resource;

import static com.db.feedhub.mapper.FeedbackMapper.map;
import static java.util.Objects.isNull;

import com.db.feedhub.mapper.FeedbackMapper;
import com.db.feedhub.model.api.FeedbackApi;
import com.db.feedhub.model.entity.Feedback;
import com.db.feedhub.service.FeedbackService;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/feedback")
@AllArgsConstructor
@Slf4j
public class FeedbackResource {

  private final FeedbackService feedbackService;

  @GetMapping(value = "/{id}",
      produces = "application/json",
      consumes = "application/json")
  public ResponseEntity<FeedbackApi> findById(@PathVariable UUID id) {
    Optional<Feedback> feedback = feedbackService.findById(id);
    return feedback.map(value -> ResponseEntity.ok(map(value)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // TODO test resource
  @GetMapping(produces = "application/json", consumes = "application/json")
  public Page<FeedbackApi> findAll(@NonNull Pageable pageable) {
    Page<Feedback> feedbackPage = feedbackService.findAll(pageable);
    return new PageImpl<>(feedbackPage.getContent().stream().map(FeedbackMapper::map).toList(),
        feedbackPage.getPageable(),
        feedbackPage.getTotalElements());
  }

  // TODO test resource
  @GetMapping(value = "/between", produces = "application/json", consumes = "application/json")
  public Page<FeedbackApi> findPageByDateBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
      @NonNull Pageable pageable) {
    Page<Feedback> feedbackPage = feedbackService.findPageByDateBetween(start,
        end,
        pageable);
    return new PageImpl<>(feedbackPage.getContent().stream().map(FeedbackMapper::map).toList(),
        feedbackPage.getPageable(),
        feedbackPage.getTotalElements());
  }

  // TODO test resource
  @GetMapping(value = "/censored", produces = "application/json", consumes = "application/json")
  @PreAuthorize("hasAuthority('FEEDBACK_READ')")
  public Page<FeedbackApi> findAllCensored(@NonNull Pageable pageable) {
    Page<Feedback> feedbackPage = feedbackService.findAllCensored(pageable);
    return new PageImpl<>(
        feedbackPage.getContent().stream().map(FeedbackMapper::mapCensored).toList(),
        feedbackPage.getPageable(),
        feedbackPage.getTotalElements());
  }

  @PostMapping(produces = "application/json", consumes = "application/json")
  public ResponseEntity<Void> create(@RequestBody FeedbackApi feedbackApi,
      UriComponentsBuilder uriBuilder) {
    if (!isNull(feedbackApi.id())) {
      log.error("Error creating a feedback, id is not null");
      return ResponseEntity.badRequest().build();
    }

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

  // TODO test resource
  @PutMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('FEEDBACK_UPDATE')")
  public ResponseEntity<Void> censorOrUncensorComment(@PathVariable UUID id) {
    Optional<Feedback> feedback = feedbackService.findById(id);

    if (feedback.isEmpty()) {
      log.error("Feedback not found with id: {}", id);
      return ResponseEntity.badRequest().build();
    }

    try {
      Feedback feedbackToUpdate = feedback.get();
      feedbackToUpdate.setCensored(!feedbackToUpdate.isCensored());

      feedbackService.update(feedbackToUpdate);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException exception) {
      log.error("Error updating a feedback", exception);
      return ResponseEntity.badRequest().build();
    }
  }

  // TODO test resource
  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('FEEDBACK_DELETE')")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    try {
      feedbackService.delete(id);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException exception) {
      log.error("Error deleting a feedback", exception);
      return ResponseEntity.badRequest().build();
    }
  }
}
