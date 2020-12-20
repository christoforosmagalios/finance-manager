package com.github.cmag.financemanager.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Authentication Filter.
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;
  private String secret;
  private int expires;
  private UserRepository userRepository;

  /**
   * AuthenticationFilter Constructor.
   *
   * @param authenticationManager The AuthenticationManager.
   * @param secret The JWT secret
   * @param expires The JWT expiration time.
   * @param userRepository The userRepository.
   */
  public AuthenticationFilter(AuthenticationManager authenticationManager, String secret,
      int expires, UserRepository userRepository) {
    this.authenticationManager = authenticationManager;
    this.secret = secret;
    this.expires = expires;
    this.userRepository = userRepository;
  }

  /**
   * Try to perform an authentication.
   *
   * @param req HttpServletRequest
   * @param res HttpServletResponse
   * @return The Authentication
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest req,
      HttpServletResponse res) throws AuthenticationException {

    com.github.cmag.financemanager.model.User user = null;
    try {
      user = new ObjectMapper().readValue(req.getInputStream(),
          com.github.cmag.financemanager.model.User.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (Objects.isNull(user)) {
      return null;
    }

    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            user.getUsername(),
            user.getPassword(),
            new ArrayList<>())
    );
  }

  /**
   * Handle successful authentication. Create a JWT token, adjust the response headers.
   *
   * @param req HttpServletRequest
   * @param res HttpServletResponse
   * @param chain FilterChain
   * @param auth Authentication
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
      FilterChain chain, Authentication auth) {
    // Generate the JWT token.
    String token = JWT.create()
        .withSubject(((User) auth.getPrincipal()).getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + expires))
        .sign(HMAC512(secret.getBytes()));
    // Get the username.
    String username = ((User) auth.getPrincipal()).getUsername();
    // Find the user with the given username.
    com.github.cmag.financemanager.model.User user = userRepository.findByUsername(username);
    // Add the response headers.
    res.addHeader(AppConstants.AUTH_HEADER_STRING, AppConstants.TOKEN_PREFIX + token);
    res.addHeader(AppConstants.AUTH_RESP_HEADER_STRING,
        user.getFirstName() + " " + user.getLastName());
  }
}
