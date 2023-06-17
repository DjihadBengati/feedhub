package com.db.feedhub.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("company")
@Getter
@Setter
public class CompanyConfig {

  @NotBlank
  private String emailPattern;
}
