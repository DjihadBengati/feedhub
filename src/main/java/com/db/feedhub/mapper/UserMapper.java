package com.db.feedhub.mapper;

import com.db.feedhub.model.api.UserApi;
import com.db.feedhub.model.entity.User;
import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

@UtilityClass
public class UserMapper {

  public static User map(@NonNull UserApi userApi) {
    return User.builder()
        .id(userApi.id())
        .email(userApi.email())
        .build();
  }

  public static UserApi map(@NonNull User user) {
    return new UserApi(user.getId(), user.getEmail());
  }
}
