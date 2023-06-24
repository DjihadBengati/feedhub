package com.db.feedhub.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.db.feedhub.model.api.UserApi;
import com.db.feedhub.model.entity.User;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

  @Test
  void map_entityToApiObject_successful() {
    UUID uuid = UUID.randomUUID();

    User user = User.builder()
        .email("test1@company.com")
        .id(uuid)
        .build();

    UserApi userApi = new UserApi(uuid, "test1@company.com");

    UserApi api = UserMapper.map(user);
    assertThat(api).usingRecursiveComparison().isEqualTo(userApi);
  }

  @Test
  void map_apiObjectEntityTo_successful() {
    UUID uuid = UUID.randomUUID();

    User user = User.builder()
        .email("test2@company.com")
        .id(uuid)
        .build();

    UserApi userApi = new UserApi(uuid, "test2@company.com");

    User entity = UserMapper.map(userApi);
    assertThat(entity).usingRecursiveComparison().isEqualTo(user);
  }
}
