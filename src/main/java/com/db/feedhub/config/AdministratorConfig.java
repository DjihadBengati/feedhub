package com.db.feedhub.config;

import com.db.feedhub.model.entity.Administrator;
import com.db.feedhub.model.entity.Role;
import com.db.feedhub.model.entity.Token;
import com.db.feedhub.model.entity.TokenType;
import com.db.feedhub.security.JwtService;
import com.db.feedhub.service.AdministratorService;
import com.db.feedhub.service.TokenService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class AdministratorConfig {

  @Bean
  public CommandLineRunner mainAdministratorConfig(CompanyConfig companyConfig,
      AdministratorService administratorService,
      JwtService jwtService,
      TokenService tokenService,
      PasswordEncoder passwordEncoder) {
    return args -> {
      Optional<Administrator> administrator =
          administratorService.findByEmail(companyConfig.getAdministratorEmail());
      if (administrator.isEmpty()) {
        String password = RandomStringUtils.random(8, true, true);

        Administrator administratorToSave = Administrator.builder()
            .role(Role.ADMIN)
            .email(companyConfig.getAdministratorEmail())
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
            companyConfig.getAdministratorEmail(),
            password);

        // TODO send email requesting password change
      } else if (!administrator.get().isPasswordUpdated()) {
        log.warn("Administrator password must be updated !");
        // TODO Send email requesting password change
      } else {
        log.info("Main administrator present and password updated !");
      }
    };
  }
}
