package com.db.feedhub.service;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.db.feedhub.config.CompanyConfig;
import com.db.feedhub.model.api.AuthenticationRequestApi;
import com.db.feedhub.model.api.AuthenticationResponseApi;
import com.db.feedhub.model.api.RegisterRequestApi;
import com.db.feedhub.model.entity.Administrator;
import com.db.feedhub.model.entity.Token;
import com.db.feedhub.model.entity.TokenType;
import com.db.feedhub.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

  private final AdministratorService administratorService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final CompanyConfig companyConfig;

  public Administrator register(@NonNull RegisterRequestApi request) {
    checkCompanyEmailPattern(request.email());

    String password = RandomStringUtils.random(8, true, true);

    Administrator administrator = administratorService.save(request.email(),
        passwordEncoder.encode(password));

    String newToken = jwtService.generateToken(administrator);
    saveAdminToken(administrator, newToken);

    log.debug("New administrator to be created email: {}, password:{}. Password must be changed",
        request.email(),
        password);

    // TODO send email to new administrator

    return administrator;
  }

  public AuthenticationResponseApi authenticate(@NonNull AuthenticationRequestApi request) {
    log.debug("Authentication request for administrator with email: {}", request.email());
    checkCompanyEmailPattern(request.email());

    val credentials = new UsernamePasswordAuthenticationToken(request.email(),
        request.password());
    authenticationManager.authenticate(credentials);

    Administrator administrator = administratorService.findByEmail(request.email())
        .orElseThrow();

    String newToken = jwtService.generateToken(administrator);
    String refreshToken = jwtService.generateRefreshToken(administrator);

    revokeAllAdminTokens(administrator);
    saveAdminToken(administrator, newToken);

    log.debug("Successful authentication for user with email: {}", administrator.getEmail());

    return new AuthenticationResponseApi(newToken, refreshToken);
  }

  private void saveAdminToken(Administrator administrator, String token) {
    tokenService.save(Token.builder()
        .admin(administrator)
        .token(token)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build());
  }

  private void revokeAllAdminTokens(Administrator administrator) {
    List<Token> validUserTokens = tokenService.findAllValidTokenByAdmin(administrator.getId());
    if (!validUserTokens.isEmpty()) {
      validUserTokens.forEach(token -> {
        token.setExpired(true);
        token.setRevoked(true);
      });
      tokenService.saveAll(validUserTokens);
    }
  }

  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    final String authHeader = request.getHeader(AUTHORIZATION);
    final String refreshToken;
    final String email;

    if (!isNull(authHeader) && authHeader.startsWith("Bearer ")) {
      refreshToken = authHeader.substring(7);
      email = jwtService.extractUsername(refreshToken);
      if (!isNull(email)) {
        Administrator administrator = administratorService.findByEmail(email)
            .orElseThrow();
        if (jwtService.isTokenValid(refreshToken, administrator)) {
          var accessToken = jwtService.generateToken(administrator);
          revokeAllAdminTokens(administrator);
          saveAdminToken(administrator, accessToken);

          log.debug("Token refreshed for user with email: {}", administrator.getEmail());

          new ObjectMapper().writeValue(response.getOutputStream(),
              new AuthenticationResponseApi(accessToken, refreshToken));
        }
      }
    }
  }

  private void checkCompanyEmailPattern(@NonNull String email) {
    if (isBlank(email) ||
        isBlank(companyConfig.getEmailPattern()) ||
        !email.endsWith(companyConfig.getEmailPattern())) {
      log.error("Invalid email: {}", email);
      throw new IllegalArgumentException("Invalid email");
    }
  }
}
