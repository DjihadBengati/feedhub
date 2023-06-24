package com.db.feedhub.mapper;

import static com.db.feedhub.data.AdministratorData.administratorApi_valid;
import static com.db.feedhub.data.AdministratorData.administrator_valid;
import static com.db.feedhub.mapper.AdministratorMapper.map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.db.feedhub.model.api.AdministratorApi;
import com.db.feedhub.model.entity.Administrator;
import org.junit.jupiter.api.Test;

public class AdministratorMapperTest {

  @Test
  void map_entityToApiObject_successful() {
    AdministratorApi api = map(administrator_valid());
    assertThat(api).usingRecursiveComparison().isEqualTo(administratorApi_valid());
  }

  @Test
  void map_apiObjectToEntity_successful() {
    Administrator entity = map(administratorApi_valid());
    assertThat(entity).usingRecursiveComparison()
        .ignoringFields("password")
        .isEqualTo(administrator_valid());
  }
}
