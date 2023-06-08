package com.db.feedhub.mapper;

import com.db.feedhub.model.api.FeedbackApi;
import com.db.feedhub.model.entity.Feedback;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FeedbackMapper {

  public static FeedbackApi map(@NonNull Feedback feedback) {
    return new FeedbackApi(feedback.getId(),
        null,
        feedback.getNote(),
        feedback.getComment(),
        feedback.getDateTime());
  }

  public static Feedback map(@NonNull FeedbackApi feedbackApi) {
    Feedback feedback = new Feedback();
    feedback.setId(feedbackApi.id());
    feedback.setNote(feedbackApi.note());
    feedback.setComment(feedbackApi.comment());
    feedback.setDateTime(feedbackApi.dateTime());
    feedback.setSessionId(feedbackApi.sessionId());
    return feedback;
  }
}
