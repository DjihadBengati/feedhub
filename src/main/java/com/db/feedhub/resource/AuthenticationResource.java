package com.db.feedhub.resource;

import com.db.feedhub.model.api.AuthenticationRequestApi;
import com.db.feedhub.model.api.AuthenticationResponseApi;
import com.db.feedhub.service.AuthenticationService;
import com.db.feedhub.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO test resource
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthenticationResource {

  private final AuthenticationService authenticationService;
  private final LogoutService logoutService;

  @PostMapping(value = "/authenticate",
      produces = "application/json",
      consumes = "application/json")
  public ResponseEntity<AuthenticationResponseApi> authenticate(
      @RequestBody AuthenticationRequestApi request) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(authenticationService.authenticate(request));
    } catch (IllegalArgumentException exception) {
      log.error("Authentication error", exception);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PostMapping(value = "/refresh-token", consumes = "application/json")
  public ResponseEntity<Void> refreshToken(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      authenticationService.refreshToken(request, response);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (Exception exception) {
      log.error("Refresh token error", exception);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PostMapping(value = "/logout", consumes = "application/json")
  public ResponseEntity<Void> logout(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) {
    try {
      logoutService.logout(request, response, authentication);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (Exception exception) {
      log.error("Logout error", exception);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}
