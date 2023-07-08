package com.db.feedhub.resource;

import static java.util.Objects.isNull;

import com.db.feedhub.mapper.AdministratorMapper;
import com.db.feedhub.model.api.AdministratorApi;
import com.db.feedhub.model.entity.Administrator;
import com.db.feedhub.service.AdministratorService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

// TODO test resource
@RestController
@RequestMapping("/api/v1/administrator")
@AllArgsConstructor
@Slf4j
public class AdministratorResource {

  private final AdministratorService administratorService;

  @PostMapping(produces = "application/json", consumes = "application/json")
  @PreAuthorize("hasAuthority('USER_CRUD')")
  public ResponseEntity<AdministratorApi> create(@RequestBody AdministratorApi administratorApi,
      UriComponentsBuilder uriBuilder) {
    try {
      if (!isNull(administratorApi.id())) {
        log.error("Error creating a administrator, id is not null");
        return ResponseEntity.badRequest().build();
      }

      Administrator user = administratorService.save(AdministratorMapper.map(administratorApi));
      String resourceUri = uriBuilder.path("/api/v1/administrator/{id}")
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

  @GetMapping(produces = "application/json")
  @PreAuthorize("hasAuthority('MANAGER_READ')")
  public Page<AdministratorApi> findAll(Pageable pageable) {
    Page<Administrator> administrators = administratorService.findAll(pageable);
    return new PageImpl<>(administrators.getContent().stream()
        .map(AdministratorMapper::map).toList(),
        administrators.getPageable(),
        administrators.getTotalElements());
  }

  @GetMapping(value = "/{id}", produces = "application/json")
  @PreAuthorize("hasAuthority('MANAGER_READ')")
  public ResponseEntity<AdministratorApi> findById(@PathVariable UUID id) {
    Optional<Administrator> administrator = administratorService.findById(id);
    return administrator.map(u -> ResponseEntity.ok(AdministratorMapper.map(u)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('MANAGER_DELETE')")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    try {
      administratorService.delete(id);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException exception) {
      log.error("Error deleting administrator", exception);
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping(value = "/role", consumes = "application/json")
  @PreAuthorize("hasAuthority('MANAGER_UPDATE')")
  public ResponseEntity<Void> updateRole(HttpServletRequest httpServletRequest,
      @RequestBody AdministratorApi administratorApi) {
    try {
      administratorService.updateRole(httpServletRequest, administratorApi.email());
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException exception) {
      log.error("Error updating administrator role", exception);
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping(value = "/password", consumes = "application/json")
  public ResponseEntity<Void> updatePassword(HttpServletRequest httpServletRequest,
      @RequestBody AdministratorApi administratorApi) {
    try {
      administratorService.updatePassword(httpServletRequest,
          administratorApi.email(),
          administratorApi.password());
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException exception) {
      log.error("Error updating administrator password", exception);
      return ResponseEntity.badRequest().build();
    }
  }
}
