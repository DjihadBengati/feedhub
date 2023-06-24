package com.db.feedhub.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedhub_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(unique = true)
  @Email
  private String email;

  private boolean unsubscribe;

  private LocalDateTime unsubscribeDateTime;

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("email", email)
        .append("unsubscribe", unsubscribe)
        .append("unsubscribeDateTime", unsubscribeDateTime)
        .toString();
  }
}
