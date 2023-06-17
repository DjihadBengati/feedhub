package com.db.feedhub.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Feedback {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(unique = true, nullable = false)
  private UUID sessionId;

  @Column(nullable = false)
  private Integer note;

  private String comment;

  private LocalDateTime dateTime = LocalDateTime.now();

  @Override
  public String toString() {
    return new org.apache.commons.lang3.builder.ToStringBuilder(this)
        .append("sessionId", sessionId)
        .append("note", note)
        .append("comment", comment)
        .append("dateTime", dateTime)
        .toString();
  }
}
