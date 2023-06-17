package com.db.feedhub.resource;

import static com.db.feedhub.data.FeedbackData.feedback_4;
import static com.db.feedhub.data.FeedbackData.feedbacks;
import static com.db.feedhub.data.SessionData.session_invalid_1;
import static com.db.feedhub.data.SessionData.session_valid_1;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import com.db.feedhub.model.api.FeedbackApi;
import com.db.feedhub.model.entity.Feedback;
import com.db.feedhub.model.entity.Session;
import com.db.feedhub.repository.FeedbackRepository;
import com.db.feedhub.repository.SessionRepository;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FeedbackResourceTest {

  @Autowired
  TestRestTemplate restTemplate;

  @Autowired
  FeedbackRepository feedbackRepository;

  @Autowired
  SessionRepository sessionRepository;

  @BeforeEach
  void setUp() {
    feedbackRepository.saveAll(feedbacks());
  }

  @AfterEach
  void clean() {
    sessionRepository.deleteAll();
    feedbackRepository.deleteAll();
  }

  @Test
  void save_successful() {
    LocalDateTime localDateTime = LocalDateTime.now();

    Session session = sessionRepository.save(session_valid_1());
    assertThat(session).isNotNull();

    ResponseEntity<Void> createResponse = restTemplate.postForEntity("/api/v1/feedback",
        new FeedbackApi(null,
            session.getId(),
            10,
            "Very good !",
            localDateTime),
        Void.class);

    assertThat(createResponse.getStatusCode().is2xxSuccessful()).isTrue();

    URI locationOfNewFeedback = createResponse.getHeaders().getLocation();
    ResponseEntity<FeedbackApi> getResponse = restTemplate.getForEntity(locationOfNewFeedback,
        FeedbackApi.class);
    assertThat(getResponse.getStatusCode().is2xxSuccessful()).isTrue();

    FeedbackApi createdFeedback = getResponse.getBody();

    assertThat(createdFeedback).isNotNull();
    assertThat(createdFeedback.id()).isNotNull();
    assertThat(createdFeedback.note()).isEqualTo(10);
    // This information is not returned by the resource for anonymity reasons
    assertThat(createdFeedback.sessionId()).isNull();
    assertThat(createdFeedback.comment()).isEqualTo("Very good !");
    assertThat(createdFeedback.dateTime().getYear()).isEqualTo(localDateTime.getYear());
    assertThat(createdFeedback.dateTime().getMonth()).isEqualTo(localDateTime.getMonth());
    assertThat(createdFeedback.dateTime().getDayOfMonth()).isEqualTo(localDateTime.getDayOfMonth());
    assertThat(createdFeedback.dateTime().getHour()).isEqualTo(localDateTime.getHour());
    assertThat(createdFeedback.dateTime().getMinute()).isEqualTo(localDateTime.getMinute());

    Optional<Session> deletedSession = sessionRepository.findById(session.getId());
    assertThat(deletedSession).isEmpty();
  }

  @Test
  void save_failed_sessionIdIsNull() {
    ResponseEntity<Void> createResponse = restTemplate.postForEntity("/api/v1/feedback",
        new FeedbackApi(null,
            null,
            10,
            "Very good !",
            LocalDateTime.now()),
        Void.class);

    assertThat(createResponse.getStatusCode().is4xxClientError()).isTrue();
  }

  @Test
  void save_failed_sessionInvalid() {
    Session session = sessionRepository.save(session_invalid_1());
    assertThat(session).isNotNull();

    ResponseEntity<Void> createResponse = restTemplate.postForEntity("/api/v1/feedback",
        new FeedbackApi(null,
            session.getId(),
            10,
            "Very good !",
            LocalDateTime.now()),
        Void.class);

    assertThat(createResponse.getStatusCode().is4xxClientError()).isTrue();
  }

  @Test
  void findById_successful() {
    Feedback savedFeedback = feedbackRepository.save(feedback_4());
    ResponseEntity<FeedbackApi> response = restTemplate.getForEntity(format("/api/v1/feedback/%s",
            savedFeedback.getId()),
        FeedbackApi.class);

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

    FeedbackApi feedback = response.getBody();
    assertThat(feedback).isNotNull();
    assertThat(feedback.id()).isNotNull();
    assertThat(feedback.note()).isEqualTo(feedback_4().getNote());
    // This information is not returned by the resource for anonymity reasons
    assertThat(feedback.sessionId()).isNull();
    assertThat(feedback.comment()).isEqualTo(feedback_4().getComment());
    assertThat(feedback.dateTime().getYear()).isEqualTo(feedback_4().getDateTime().getYear());
    assertThat(feedback.dateTime().getMonth()).isEqualTo(feedback_4().getDateTime().getMonth());
    assertThat(feedback.dateTime().getDayOfMonth()).isEqualTo(
        feedback_4().getDateTime().getDayOfMonth());
    assertThat(feedback.dateTime().getHour()).isEqualTo(feedback_4().getDateTime().getHour());
    assertThat(feedback.dateTime().getMinute()).isEqualTo(feedback_4().getDateTime().getMinute());
  }

  @Test
  void findById_failed_notFound() {
    ResponseEntity<FeedbackApi> response = restTemplate.getForEntity(format("/api/v1/feedback/%s",
            randomUUID()),
        FeedbackApi.class);

    assertThat(response.getStatusCode().is4xxClientError()).isTrue();
  }

}
