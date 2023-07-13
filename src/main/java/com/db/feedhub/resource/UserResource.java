package com.db.feedhub.resource;

import static java.util.Objects.isNull;

import com.db.feedhub.mapper.UserMapper;
import com.db.feedhub.model.api.UserApi;
import com.db.feedhub.model.entity.User;
import com.db.feedhub.service.UserService;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

// TODO test this resource
@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Slf4j
public class UserResource {

  private final UserService userService;

  @GetMapping(value = "/{id}", produces = "application/json")
  @PreAuthorize("hasAuthority('USER_CRUD')")
  public ResponseEntity<UserApi> findById(@PathVariable UUID id) {
    Optional<User> user = userService.findById(id);
    return user.map(u -> ResponseEntity.ok(UserMapper.map(u)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // TODO test resource
  @GetMapping(produces = "application/json", consumes = "application/json")
  @PreAuthorize("hasAuthority('USER_CRUD')")
  public Page<UserApi> findAll(Pageable pageable) {
    Page<User> usersPage = userService.findAll(pageable);
    return new PageImpl<>(usersPage.getContent().stream().map(UserMapper::map).toList(),
        usersPage.getPageable(),
        usersPage.getTotalElements());
  }

  @PostMapping(produces = "application/json", consumes = "application/json")
  @PreAuthorize("hasAuthority('USER_CRUD')")
  public ResponseEntity<UserApi> create(@RequestBody UserApi userApi,
      UriComponentsBuilder uriBuilder) {
    try {
      if (!isNull(userApi.id())) {
        log.error("Error creating a user, id is not null");
        return ResponseEntity.badRequest().build();
      }

      User user = userService.save(UserMapper.map(userApi));
      String resourceUri = uriBuilder.path("/api/v1/user/{id}")
          .buildAndExpand(user.getId())
          .toUriString();
      return ResponseEntity.created(UriComponentsBuilder
              .fromUriString(resourceUri)
              .build().toUri())
          .build();
    } catch (IllegalArgumentException exception) {
      log.error("Error creating a user", exception);
      return ResponseEntity.badRequest().build();
    }
  }

  // TODO test resource
  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('USER_CRUD')")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    try {
      userService.delete(id);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException exception) {
      log.error("Error deleting a feedback", exception);
      return ResponseEntity.badRequest().build();
    }
  }
}
