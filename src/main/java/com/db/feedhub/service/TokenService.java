package com.db.feedhub.service;

import com.db.feedhub.model.entity.Token;
import com.db.feedhub.repository.TokenRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TokenService {

  private final TokenRepository tokenRepository;

  // TODO test function
  public Optional<Token> findByToken(@NonNull String token) {
    log.debug("Getting token starts with: {}", token.substring(0, 10).concat("****"));
    return tokenRepository.findByToken(token);
  }

  // TODO test function
  public Token save(@NonNull Token token) {
    log.debug("Creating token {}", token);
    return tokenRepository.save(token);
  }

  // TODO test function
  public boolean isTokenValid(@NonNull String token) {
    log.debug("Checking token validity: {}", token.substring(0, 10).concat("****"));
    return tokenRepository.findByToken(token)
        .map(t -> !t.isExpired() && !t.isRevoked())
        .orElse(false);
  }

  // TODO test function
  public void saveAll(@NonNull List<Token> tokens) {
    log.debug("Saving {} token(s)", tokens.size());
    tokenRepository.saveAll(tokens);
  }

  public List<Token> findAllValidTokenByAdmin(UUID adminId) {
    log.debug("Getting all valid tokens for administrator id: {}", adminId.toString());
    return tokenRepository.findAllValidTokenByAdmin(adminId);
  }

}
