package com.db.feedhub.mapper;

import com.db.feedhub.model.api.FeedbackApi;
import com.db.feedhub.model.entity.Feedback;
import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

@UtilityClass
public class FeedbackMapper {

  public static FeedbackApi map(@NonNull Feedback feedback) {
    return new FeedbackApi(feedback.getId(),
        null,
        feedback.getNote(),
        !feedback.isCensored() ? feedback.getComment() : "****",
        feedback.getDateTime());
  }

  public static FeedbackApi mapCensored(@NonNull Feedback feedback) {
    return new FeedbackApi(feedback.getId(),
        null,
        feedback.getNote(),
        feedback.getComment(),
        feedback.getDateTime());
  }

  public static Feedback map(@NonNull FeedbackApi feedbackApi) {
    return Feedback.builder()
        .id(feedbackApi.id())
        .note(feedbackApi.note())
        .comment(feedbackApi.comment())
        .dateTime(feedbackApi.dateTime())
        .sessionId(feedbackApi.sessionId())
        .build();
  }
}
