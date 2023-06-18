package com.db.feedhub.resource;

import com.db.feedhub.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/session")
public class SessionResource {

  private final SessionService sessionService;

  public SessionResource(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  // TODO test resource
  @PostMapping("/generate")
  public ResponseEntity<Void> generateSessions() {
    sessionService.generateSessions();
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
