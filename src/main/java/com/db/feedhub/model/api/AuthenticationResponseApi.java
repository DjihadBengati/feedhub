package com.db.feedhub.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationResponseApi(@JsonProperty("access_token") String accessToken,
                                        @JsonProperty("refresh_token") String refreshToken) {

}
