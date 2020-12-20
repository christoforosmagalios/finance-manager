package com.github.cmag.financemanager.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.cmag.financemanager.config.AppConstants;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Authorization Filter.
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

  private String secret;

  /**
   * AuthorizationFilter Constructor.
   *
   * @param authManager AuthenticationManager.
   * @param secret The JWT secret.
   */
  public AuthorizationFilter(AuthenticationManager authManager, String secret) {
    super(authManager);
    this.secret = secret;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
      FilterChain chain) throws IOException, ServletException {
    // Get the token from the Authorization header.
    String header = req.getHeader(AppConstants.AUTH_HEADER_STRING);
    if (header == null || !header.startsWith(AppConstants.TOKEN_PREFIX)) {
      chain.doFilter(req, res);
      return;
    }

    // Get authentication.
    UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
    // Set the authentication.
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  /**
   * Get the token from the request, validate it and create a Authentication Token.
   *
   * @param request The request.
   * @return Authentication token.
   */
  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    // Get the token from the Authorization header.
    String token = request.getHeader(AppConstants.AUTH_HEADER_STRING);
    if (token != null) {
      // Parse the token.
      String user = JWT.require(Algorithm.HMAC512(secret.getBytes()))
          .build()
          .verify(token.replace(AppConstants.TOKEN_PREFIX, ""))
          .getSubject();

      if (user != null) {
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
      }
      return null;
    }
    return null;
  }
}
