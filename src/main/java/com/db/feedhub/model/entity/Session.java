package com.db.feedhub.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedhub_session")
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Default
  private LocalDateTime startDateTime = LocalDateTime.now();

  @Default
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
