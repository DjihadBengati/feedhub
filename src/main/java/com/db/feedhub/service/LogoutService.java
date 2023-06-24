package com.db.feedhub.service;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.db.feedhub.model.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private final TokenService tokenService;

  @Override
  public void logout(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) {
    final String authHeader = request.getHeader(AUTHORIZATION);
    final String jwt;

    if (!isNull(authHeader) && authHeader.startsWith("Bearer ")) {
      jwt = authHeader.substring(7);

      Optional<Token> storedToken = tokenService.findByToken(jwt);

      if (storedToken.isPresent()) {
        Token token = storedToken.get();
        token.setExpired(true);
        token.setRevoked(true);
        tokenService.save(token);
        SecurityContextHolder.clearContext();
      }
    }
  }
}
