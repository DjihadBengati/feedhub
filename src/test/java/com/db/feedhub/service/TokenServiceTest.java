package com.db.feedhub.service;

import static org.assertj.core.api.Assertions.assertThat;
import static com.db.feedhub.model.entity.Role.ADMIN;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.db.feedhub.model.entity.Administrator;
import com.db.feedhub.model.entity.Token;
import com.db.feedhub.model.entity.TokenType;
import com.db.feedhub.repository.AdministratorRepository;
import com.db.feedhub.repository.TokenRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TokenServiceTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AdministratorRepository administratorRepository;

    @BeforeEach
    void setUp() {
        administratorRepository.save(Administrator.builder()
                .email("adm@company.com")
                .password("1234")
                .passwordUpdated(true)
                .role(ADMIN)
                .build());
    }

    @AfterEach
    void clean() {
        tokenRepository.deleteAll();
        administratorRepository.deleteAll();
    }

    @Test
    void save_successful() {
        long countBeforeSave = tokenRepository.count();
        Optional<Administrator> administrator = administratorRepository.findByEmail("adm@company.com");
        assertThat(administrator).isNotEmpty();

        Token token = tokenService.save(Token.builder()
                .tokenType(TokenType.BEARER)
                .admin(administrator.get())
                .token("kjhbjnllkjbhbjkbjnjknkjkn")
                .build());

        assertThat(token).isNotNull();
        assertThat(tokenRepository.count()).isEqualTo(countBeforeSave + 1);

        assertThat(token.admin.getId()).isEqualTo(administrator.get().getId());
        assertThat(token.getToken()).isEqualTo("kjhbjnllkjbhbjkbjnjknkjkn");
        assertThat(token.getTokenType()).isEqualTo(TokenType.BEARER);
        assertThat(token.isExpired()).isFalse();
        assertThat(token.isRevoked()).isFalse();

        List<Token> tokens = tokenService.findAllValidTokenByAdmin(administrator.get().getId());
        assertThat(tokens).hasSize(1);
        assertThat(tokens.get(0).getToken()).isEqualTo(token.getToken());
    }

}