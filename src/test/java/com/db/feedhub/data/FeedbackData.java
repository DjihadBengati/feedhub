package com.db.feedhub.data;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

import com.db.feedhub.model.api.FeedbackApi;
import com.db.feedhub.model.entity.Feedback;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FeedbackData {

  private final UUID id = randomUUID();
  private final UUID sessionId = randomUUID();
  private final LocalDateTime dateTime = now();

  public static List<Feedback> feedbacks() {
    return List.of(feedback(), feedback_2(), feedback_3());
  }

  public static Feedback feedback() {
    Feedback feedback = new Feedback();
    feedback.setId(id);
    feedback.setSessionId(sessionId);
    feedback.setDateTime(dateTime);
    feedback.setComment("Excellent !");
    feedback.setNote(10);
    return feedback;
  }

  public static Feedback feedback_2() {
    Feedback feedback = new Feedback();
    feedback.setId(randomUUID());
    feedback.setSessionId(randomUUID());
    feedback.setDateTime(now());
    feedback.setComment("Not bad !");
    feedback.setNote(7);
    return feedback;
  }

  public static Feedback feedback_3() {
    Feedback feedback = new Feedback();
    feedback.setId(randomUUID());
    feedback.setSessionId(randomUUID());
    feedback.setDateTime(now());
    feedback.setComment("Not my day !");
    feedback.setNote(3);
    return feedback;
  }

  public static Feedback feedback_4() {
    Feedback feedback = new Feedback();
    feedback.setId(randomUUID());
    feedback.setSessionId(randomUUID());
    feedback.setDateTime(now());
    feedback.setComment("Good !");
    feedback.setNote(5);
    return feedback;
  }

  public static FeedbackApi feedbackApi() {
    return new FeedbackApi(id,
        sessionId,
        10,
        "Excellent !",
        dateTime);
  }
}
