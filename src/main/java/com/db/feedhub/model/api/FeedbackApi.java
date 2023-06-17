package com.db.feedhub.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public record FeedbackApi(UUID id,
                          UUID sessionId,
                          Integer note,
                          String comment,
                          LocalDateTime dateTime) {

}
