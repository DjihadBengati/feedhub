package com.db.feedhub.model.api;

import com.db.feedhub.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public record AdministratorApi(UUID id,
                               String email,
                               String password,
                               Role role,
                               boolean passwordUpdated) {

}
