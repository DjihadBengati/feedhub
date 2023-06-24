package com.db.feedhub.security;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.db.feedhub.model.entity.Administrator;
import com.db.feedhub.service.AdministratorService;
import com.db.feedhub.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  private final AdministratorService administratorService;

  private final TokenService tokenService;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader(AUTHORIZATION);
    final String jwt;
    final String email;
    if (isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
    } else {
      jwt = authHeader.substring(7);
      email = jwtService.extractUsername(jwt);

      if (!isNull(email) && isNull(SecurityContextHolder.getContext().getAuthentication())) {
        Optional<Administrator> administrator = administratorService.findByEmail(email);
        if (administrator.isPresent() &&
            tokenService.isTokenValid(jwt) &&
            jwtService.isTokenValid(jwt, administrator.get())) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              administrator.get(),
              null,
              administrator.get().getAuthorities()
          );
          authToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request)
          );
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
      filterChain.doFilter(request, response);
    }
  }
}
