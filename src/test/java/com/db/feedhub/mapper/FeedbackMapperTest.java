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
  void map_apiObjectEntityTo_successful() {
    Feedback entity = FeedbackMapper.map(feedbackApi());
    assertThat(entity).usingRecursiveComparison()
        .isEqualTo(feedback());
  }
}
