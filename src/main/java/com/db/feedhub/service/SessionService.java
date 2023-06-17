package com.db.feedhub.service;

import com.db.feedhub.model.entity.Session;
import com.db.feedhub.repository.SessionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SessionService {

  private final SessionRepository sessionRepository;

  public SessionService(@NonNull SessionRepository repository) {
    this.sessionRepository = repository;
  }

  // TODO test this function
  public Session save(Session session) {
    log.debug("Saving new session: {}", session);
    return sessionRepository.save(session);
  }

  // TODO test this function
  public List<Session> saveAll(@NonNull List<Session> sessions) {
    log.debug("Saving {} session(s)", sessions.size());
    return sessionRepository.saveAll(sessions);
  }

  // TODO test this function
  public boolean isValidSession(@NonNull String sessionId) {
    log.debug("Checking validity of session with ID: {}", sessionId);
    Optional<Session> session = sessionRepository.findById(UUID.fromString(sessionId));
    return session.isPresent() &&
        session.get().getStartDateTime().isBefore(LocalDateTime.now()) &&
        session.get().getEndDateTime().isAfter(LocalDateTime.now());
  }

  // TODO test this function
  public void delete(@NonNull String sessionId) {
    log.debug("Deleting session with ID: {}", sessionId);
    sessionRepository.deleteById(UUID.fromString(sessionId));
  }
}