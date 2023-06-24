package com.db.feedhub.resource;

import com.db.feedhub.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO test this resource
@RestController
@RequestMapping("/api/v1/session")
@AllArgsConstructor
public class SessionResource {

  private final SessionService sessionService;

  @PostMapping(value = "/generate")
  @PreAuthorize("hasAuthority('SESSION_CREATE')")
  public ResponseEntity<Void> generateSessions() {
    sessionService.generateSessions();
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
