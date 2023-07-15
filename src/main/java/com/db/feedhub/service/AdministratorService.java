package com.db.feedhub.service;

import static com.db.feedhub.model.entity.Role.ADMIN;
import static com.db.feedhub.model.entity.Role.MANAGER;
import static com.db.feedhub.util.CompanyUtils.isValidEmail;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.db.feedhub.config.CompanyConfig;
import com.db.feedhub.model.entity.Administrator;
import com.db.feedhub.repository.AdministratorRepository;
import com.db.feedhub.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AdministratorService {

  private final AdministratorRepository administratorRepository;

  private final CompanyConfig companyConfig;

  private final JwtService jwtService;

  private final PasswordEncoder passwordEncoder;

  // TODO test function
  public Optional<Administrator> findByEmail(@NonNull String email) {
    log.debug("Getting administrator by email: {}", email);
    if (!isValidEmail(email, companyConfig.getEmailPattern())) {
      log.error("Invalid email: {}", email);
      throw new IllegalArgumentException("Invalid email");
    }

    return administratorRepository.findByEmail(email);
  }

  // TODO test function
  public Administrator save(@NonNull String email, @NonNull String password) {
    log.debug("Creating administrator with email: {}", email);
    if (!isValidEmail(email, companyConfig.getEmailPattern())) {
      log.error("Invalid email: {}", email);
      throw new IllegalArgumentException("Invalid email");
    }

    return administratorRepository.save(Administrator.builder()
        .email(email)
        .password(password)
        .role(MANAGER)
        .build());
  }

  // TODO test function
  public Administrator save(@NonNull Administrator administrator) {
    log.debug("Creating administrator with email: {}", administrator.getEmail());
    if (!isValidEmail(administrator.getEmail(), companyConfig.getEmailPattern())) {
      log.error("Invalid email: {}", administrator.getEmail());
      throw new IllegalArgumentException("Invalid email");
    }

    return administratorRepository.save(administrator);
  }

  // TODO test function
  public Page<Administrator> findAll(@NonNull Pageable pageable) {
    log.debug("Find all administrators");
    return administratorRepository.findAll(pageable);
  }

  // TODO test function
  public Optional<Administrator> findById(@NonNull UUID id) {
    log.debug("Find administrator by id: {}", id);
    return administratorRepository.findById(id);
  }

  // TODO test function
  public void delete(@NonNull UUID id) {
    log.debug("Deleting administrator with ID: {}", id);
    administratorRepository.deleteById(id);
  }

  // TODO test function
  public void updateRole(@NonNull HttpServletRequest httpRequest,
      @NonNull String email) {
    if (!isValidEmail(email, companyConfig.getEmailPattern())) {
      log.error("Invalid email: {}", email);
      throw new IllegalArgumentException("Invalid email");
    }

    final String authHeader = httpRequest.getHeader(AUTHORIZATION);
    final String refreshToken;
    final String emailFromToken;

    if (!isNull(authHeader) && authHeader.startsWith("Bearer ")) {
      refreshToken = authHeader.substring(7);
      emailFromToken = jwtService.extractUsername(refreshToken);

      // TODO change role for requested email and not the authenticated administrator
      if (!isNull(emailFromToken) &&
          emailFromToken.equalsIgnoreCase(email)) {
        Optional<Administrator> administrator = administratorRepository.findByEmail(email);
        if (administrator.isEmpty()) {
          log.error("No administrator with email: {}", email);
          throw new IllegalArgumentException(format("No administrator with email: %s", email));
        }

        Administrator entity = administrator.get();
        entity.setRole(ADMIN.equals(entity.getRole()) ? MANAGER : ADMIN);
        administratorRepository.save(entity);
      }
    }
  }

  // TODO test function
  public void updatePassword(@NonNull HttpServletRequest httpRequest,
      @NonNull String email,
      @NonNull String password) {
    log.debug("Updating password for administrator with email: {}", email);

    if (!isValidEmail(email, companyConfig.getEmailPattern())) {
      log.error("Invalid email: {}", email);
      throw new IllegalArgumentException("Invalid email");
    }

    if (isBlank(password)) {
      log.error("Invalid password: {}", password);
      throw new IllegalArgumentException("Invalid password");
    }

    final String authHeader = httpRequest.getHeader(AUTHORIZATION);
    final String refreshToken;
    final String emailFromToken;

    if (!isNull(authHeader) && authHeader.startsWith("Bearer ")) {
      refreshToken = authHeader.substring(7);
      emailFromToken = jwtService.extractUsername(refreshToken);
      if (!isNull(emailFromToken) &&
          emailFromToken.equalsIgnoreCase(email)) {
        Optional<Administrator> administrator = administratorRepository.findByEmail(email);
        if (administrator.isEmpty()) {
          log.error("No administrator with email: {}", email);
          throw new IllegalArgumentException(format("No administrator with email: %s", email));
        }

        Administrator entity = administrator.get();
        entity.setPassword(passwordEncoder.encode(password));
        entity.setPasswordUpdated(true);
        administratorRepository.save(entity);
      }
    }
  }
}
