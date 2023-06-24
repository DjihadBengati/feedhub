package com.db.feedhub.model.entity;

import static com.db.feedhub.model.entity.Permission.FEEDBACK_DELETE;
import static com.db.feedhub.model.entity.Permission.FEEDBACK_READ;
import static com.db.feedhub.model.entity.Permission.FEEDBACK_UPDATE;
import static com.db.feedhub.model.entity.Permission.MANAGER_CREATE;
import static com.db.feedhub.model.entity.Permission.MANAGER_DELETE;
import static com.db.feedhub.model.entity.Permission.MANAGER_READ;
import static com.db.feedhub.model.entity.Permission.MANAGER_UPDATE;
import static com.db.feedhub.model.entity.Permission.SESSION_CREATE;
import static com.db.feedhub.model.entity.Permission.USER_CRUD;
import static java.util.Set.of;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AllArgsConstructor
public enum Role {
  ADMIN(of(MANAGER_READ,
      MANAGER_CREATE,
      MANAGER_UPDATE,
      MANAGER_DELETE,
      USER_CRUD,
      FEEDBACK_READ,
      FEEDBACK_UPDATE,
      FEEDBACK_DELETE,
      SESSION_CREATE)),
  MANAGER(of(USER_CRUD,
      MANAGER_READ,
      FEEDBACK_READ,
      FEEDBACK_UPDATE,
      SESSION_CREATE));

  @Getter
  private Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    return getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.name())).toList();
  }
}
