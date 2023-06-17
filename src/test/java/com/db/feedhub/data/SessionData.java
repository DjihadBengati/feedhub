package com.db.feedhub.data;

import com.db.feedhub.model.entity.Session;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SessionData {

  public static Session session_valid_1() {
    return new Session();
  }

  public static Session session_invalid_1() {
    Session session = new Session();
    session.setStartDateTime(LocalDateTime.of(LocalDate.now().minusDays(1),
        LocalTime.MIN));
    session.setEndDateTime(LocalDateTime.of(LocalDate.now().minusDays(1),
        LocalTime.MAX));
    return session;
  }
}
