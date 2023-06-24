package com.db.feedhub.mapper;

import com.db.feedhub.model.api.AdministratorApi;
import com.db.feedhub.model.entity.Administrator;
import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

// TODO test utility class
@UtilityClass
public class AdministratorMapper {

  public static Administrator map(@NonNull AdministratorApi administratorApi) {
    return Administrator.builder()
        .id(administratorApi.id())
        .email(administratorApi.email())
        .role(administratorApi.role())
        .passwordUpdated(administratorApi.passwordUpdated())
        .build();
  }

  public static AdministratorApi map(@NonNull Administrator administrator) {
    return new AdministratorApi(administrator.getId(),
        administrator.getEmail(),
        null,
        administrator.getRole(),
        administrator.isPasswordUpdated());
  }
}
