package com.db.feedhub.resource;

import static org.assertj.core.api.Assertions.assertThat;

import com.db.feedhub.model.api.FeedbackApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdministratorResourceTest {

  @Autowired
  TestRestTemplate restTemplate;

  @Test
  void findAll_forbidden() {
    RequestEntity<Void> requestEntity = RequestEntity.get("/api/v1/administrator")
        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .build();
    ResponseEntity<FeedbackApi> getResponse =
        restTemplate.exchange(requestEntity, FeedbackApi.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }
}
