package com.db.feedhub.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;


public class CompanyUtilsTest {

  @Test
  void isValidEmail_successful() {
    assertThat(CompanyUtils.isValidEmail("test@company.com",
        "@company.com")).isTrue();
  }

  @Test
  void isValidEmail_failed() {
    assertThat(CompanyUtils.isValidEmail("test@google.com",
        "@company.com")).isFalse();
  }

  @Test
  void isValidEmail_failed_emptyEmail() {
    assertThat(CompanyUtils.isValidEmail("", "@company.com")).isFalse();
  }

  @Test
  void isValidEmail_failed_emptyPattern() {
    assertThat(CompanyUtils.isValidEmail("test@google.com", "")).isFalse();
  }

  @Test
  void isValidEmail_failed_nullEmail() {
    assertThat(CompanyUtils.isValidEmail(null, "@company.com")).isFalse();
  }

  @Test
  void isValidEmail_failed_nullPattern() {
    assertThat(CompanyUtils.isValidEmail("test@google.com", null)).isFalse();
  }
}
