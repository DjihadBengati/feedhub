package com.db.feedhub.security;

import static com.db.feedhub.model.entity.Permission.FEEDBACK_DELETE;
import static com.db.feedhub.model.entity.Permission.FEEDBACK_READ;
import static com.db.feedhub.model.entity.Permission.FEEDBACK_UPDATE;
import static com.db.feedhub.model.entity.Permission.MANAGER_READ;
import static com.db.feedhub.model.entity.Permission.SESSION_CREATE;
import static com.db.feedhub.model.entity.Permission.USER_CRUD;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtFilter jwtFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  // TODO check that al urls are covered
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .cors()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers("/api/v1/user/**")
        .hasAnyAuthority(USER_CRUD.name())
        .requestMatchers(POST, "/api/v1/session/**")
        .hasAnyAuthority(SESSION_CREATE.name())
        .requestMatchers(PUT, "/api/v1/feedback/**")
        .hasAnyAuthority(FEEDBACK_UPDATE.name())
        .requestMatchers(GET, "/api/v1/administrator/**")
        .hasAnyAuthority(MANAGER_READ.name())
        .requestMatchers(DELETE, "/api/v1/feedback/**")
        .hasAnyAuthority(FEEDBACK_DELETE.name())
        .requestMatchers("/api/v1/feedback/censored")
        .hasAnyAuthority(FEEDBACK_READ.name())
        .requestMatchers("/api/v1/administrator/password")
        .authenticated()
        .anyRequest()
        .permitAll()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutUrl("/api/v1/auth/logout")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler(
            (request, response, authentication) -> SecurityContextHolder.clearContext());
    return http.build();
  }

}
