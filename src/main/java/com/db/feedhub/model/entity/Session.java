package com.db.feedhub.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private LocalDateTime startDateTime = LocalDateTime.now();

  private LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id.toString())
        .append("startDateTime", startDateTime)
        .append("endDateTime", endDateTime)
        .toString();
  }
}
