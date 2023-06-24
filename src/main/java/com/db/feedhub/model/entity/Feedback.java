package com.db.feedhub.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedhub_feedback")
public class Feedback {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(unique = true, nullable = false)
  private UUID sessionId;

  @Column(nullable = false)
  private Integer note;

  private String comment;

  @Default
  private LocalDateTime dateTime = LocalDateTime.now();

  private boolean censored;

  @Override
  public String toString() {
    return new org.apache.commons.lang3.builder.ToStringBuilder(this)
        .append("sessionId", sessionId)
        .append("note", note)
        .append("comment", comment)
        .append("dateTime", dateTime)
        .append("censored", censored)
        .toString();
  }
}
