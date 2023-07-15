package com.db.feedhub.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("application.company")
@Getter
@Setter
@Validated
public class CompanyConfig {

  @NotBlank
  private String emailPattern;

  @NotBlank
  private String administratorEmail;
}
