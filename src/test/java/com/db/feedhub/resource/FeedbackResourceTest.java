package com.db.feedhub.resource;

import static com.db.feedhub.data.FeedbackData.feedbacks;
import static org.assertj.core.api.Assertions.assertThat;

import com.db.feedhub.model.api.FeedbackApi;
import com.db.feedhub.repository.FeedbackRepository;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FeedbackResourceTest {

  @Autowired
  TestRestTemplate restTemplate;

  @Autowired
  FeedbackRepository feedbackRepository;

  @BeforeEach
  void setUp() {
    feedbackRepository.saveAll(feedbacks());
  }

  @Test
  void save_successful() {
    LocalDateTime localDateTime = LocalDateTime.now();
    UUID sessionId = UUID.randomUUID();

    ResponseEntity<Void> createResponse = restTemplate.postForEntity("/api/v1/feedback",
        new FeedbackApi(null,
            sessionId,
            10,
            "Very good !",
            localDateTime),
        Void.class);

    assertThat(createResponse.getStatusCode().is2xxSuccessful()).isTrue();

    URI locationOfNewFeedback = createResponse.getHeaders().getLocation();
    ResponseEntity<FeedbackApi> getResponse = restTemplate.getForEntity(locationOfNewFeedback,
        FeedbackApi.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

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
    assertThat(createdFeedback.dateTime().getSecond()).isEqualTo(localDateTime.getSecond());
  }

}
