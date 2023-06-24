package com.db.feedhub.util;

import static org.apache.commons.lang3.StringUtils.isBlank;

import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

@UtilityClass
public class CompanyUtils {

  public static boolean isValidEmail(@NonNull String email, @NonNull String pattern) {
    return !isBlank(email) && !isBlank(pattern) && email.endsWith(pattern);
  }
}
