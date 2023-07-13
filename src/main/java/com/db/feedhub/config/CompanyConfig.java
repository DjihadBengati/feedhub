package com.db.feedhub.config;

import com.db.feedhub.model.entity.Administrator;
import com.db.feedhub.model.entity.Role;
import com.db.feedhub.model.entity.Token;
import com.db.feedhub.model.entity.TokenType;
import com.db.feedhub.security.JwtService;
import com.db.feedhub.service.AdministratorService;
import com.db.feedhub.service.TokenService;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties("application.company")
@Getter
@Setter
@Slf4j
@Validated
public class CompanyConfig {

  @NotBlank
  private String emailPattern;

  @NotBlank
  private String administratorEmail;

  @Bean
  public CommandLineRunner commandLineRunner(AdministratorService administratorService,
      JwtService jwtService,
      TokenService tokenService,
      PasswordEncoder passwordEncoder) {
    return args -> {
      Optional<Administrator> administrator = administratorService.findByEmail(administratorEmail);
      if (administrator.isEmpty()) {
        String password = RandomStringUtils.random(8, true, true);

        Administrator administratorToSave = Administrator.builder()
            .role(Role.ADMIN)
            .email(administratorEmail)
            .password(passwordEncoder.encode(password))
            .build();

        Administrator savedAdministrator = administratorService.save(administratorToSave);

        String jwtToken = jwtService.generateToken(savedAdministrator);
        Token token = tokenService.save(Token.builder()
            .admin(savedAdministrator)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build());

        tokenService.save(token);

        log.warn("New administrator to be created email: {}, password:{}. Password must be changed",
            administratorEmail,
            password);

        // TODO send email requesting password change
      } else if (!administrator.get().isPasswordUpdated()) {
        log.warn("Administrator password must be updated !");
        // TODO Send email requesting password change
      }
    };
  }
}
