package com.db.feedhub.data;

import static com.db.feedhub.model.entity.Role.ADMIN;
import static com.db.feedhub.model.entity.Role.MANAGER;

import com.db.feedhub.model.api.AdministratorApi;
import com.db.feedhub.model.entity.Administrator;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AdministratorData {

  private final UUID validAdministratorId = UUID.randomUUID();
  private final UUID validManagerId = UUID.randomUUID();
  private final UUID invalidAdministratorId = UUID.randomUUID();
  private final UUID invalidManagerId = UUID.randomUUID();

  public static Administrator administrator_valid() {
    return Administrator.builder()
        .id(validAdministratorId)
        .email("valid_administrator@company.com")
        .password("1234")
        .role(ADMIN)
        .passwordUpdated(true)
        .build();
  }

  public static AdministratorApi administratorApi_valid() {
    return new AdministratorApi(validAdministratorId,
        "valid_administrator@company.com",
        null,
        ADMIN,
        true);
  }

  public static Administrator manager_valid() {
    return Administrator.builder()
        .id(validManagerId)
        .email("valid_manager@company.com")
        .password("4321")
        .role(MANAGER)
        .passwordUpdated(true)
        .build();
  }

  public static AdministratorApi managerApi_valid() {
    return new AdministratorApi(validManagerId,
        "valid_manager@company.com",
        null,
        MANAGER,
        true);
  }

  public static Administrator administrator_invalid() {
    return Administrator.builder()
        .id(invalidAdministratorId)
        .email("invalid_administrator@company.com")
        .password("12345")
        .role(ADMIN)
        .passwordUpdated(false)
        .build();
  }

  public static AdministratorApi administratorApi_invalid() {
    return new AdministratorApi(invalidAdministratorId,
        "invalid_administrator@company.com",
        null,
        ADMIN,
        false);
  }

  public static Administrator manager_invalid() {
    return Administrator.builder()
        .id(invalidManagerId)
        .email("invalid_manager@company.com")
        .password("54321")
        .role(MANAGER)
        .passwordUpdated(false)
        .build();
  }

  public static AdministratorApi managerApi_invalid() {
    return new AdministratorApi(invalidManagerId,
        "invalid_manager@company.com",
        null,
        MANAGER,
        false);
  }
}
