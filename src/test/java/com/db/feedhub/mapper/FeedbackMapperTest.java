package com.db.feedhub.mapper;

import static com.db.feedhub.data.FeedbackData.feedback;
import static com.db.feedhub.data.FeedbackData.feedbackApi;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.db.feedhub.model.api.FeedbackApi;
import com.db.feedhub.model.entity.Feedback;
import org.junit.jupiter.api.Test;

public class FeedbackMapperTest {

  @Test
  void map_entityToApiObject_successful() {
    FeedbackApi api = FeedbackMapper.map(feedback());
    assertThat(api).usingRecursiveComparison()
        .ignoringFields("sessionId")
        .isEqualTo(feedbackApi());
  }

  @Test
  void map_apiObjectToEntity_successful() {
    Feedback entity = FeedbackMapper.map(feedbackApi());
    assertThat(entity).usingRecursiveComparison()
        .isEqualTo(feedback());
  }

  @Test
  void map_censoredEntityToApiObject_successful() {
    Feedback feedback = feedback();
    feedback.setCensored(true);
    FeedbackApi api = FeedbackMapper.map(feedback);
    assertThat(api).usingRecursiveComparison()
        .ignoringFields("sessionId", "comment")
        .isEqualTo(feedbackApi());
    assertThat(api.comment()).isEqualTo("****");
  }
}
