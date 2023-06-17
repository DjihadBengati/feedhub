package com.db.feedhub.data;

import static java.time.LocalDateTime.now;

import com.db.feedhub.model.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserData {

  public static User user_subscribed() {
    User user = new User();
    user.setEmail("test1@company.com");
    return user;
  }

  public static User user_unsubscribed() {
    User user = new User();
    user.setEmail("test2@company.com");
    user.setUnsubscribe(true);
    user.setUnsubscribeDateTime(now());
    return user;
  }
}
