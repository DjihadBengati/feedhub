package com.db.feedhub.service;

import static java.util.Collections.shuffle;
import static org.springframework.data.domain.Pageable.ofSize;

import com.db.feedhub.model.entity.Session;
import com.db.feedhub.model.entity.User;
import com.db.feedhub.repository.SessionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SessionService {

  private final SessionRepository sessionRepository;

  private final UserService userService;

  public SessionService(@NonNull SessionRepository repository, UserService userService) {
    this.sessionRepository = repository;
    this.userService = userService;
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
  public Page<Session> findAll(Pageable pageable) {
    log.debug("Find all sessions");
    return sessionRepository.findAll(pageable);
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

  // TODO test this function
  public void generateSessions() {
    log.debug("Generating new sessions");

    // Remove old sessions
    sessionRepository.deleteAll();

    // Generate sessions for users
    generateSessionsForUsers(userService.findAll(ofSize(50)));

    log.debug("{} session(s) generated", sessionRepository.count());
  }

  private void generateSessionsForUsers(Page<User> usersPage) {
    List<User> usersList = usersPage.getContent();

    shuffle(usersList);

    List<Session> sessionsToSave = new ArrayList<>();

    // Create sessions or subscribed users
    usersList.forEach(u -> {
      if (!u.isUnsubscribe()) {
        sessionsToSave.add(new Session());
      }
    });

    if (!sessionsToSave.isEmpty()) {
      sessionRepository.saveAll(sessionsToSave);
    }

    if (usersPage.hasNext()) {
      generateSessionsForUsers(userService.findAll(usersPage.nextPageable()));
    }
  }
}
