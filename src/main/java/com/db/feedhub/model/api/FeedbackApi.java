package com.db.feedhub.model.api;

import java.time.LocalDateTime;
import java.util.UUID;

public record FeedbackApi(UUID id,
                          UUID sessionId,
                          Integer note,
                          String comment,
                          LocalDateTime dateTime) {

}
