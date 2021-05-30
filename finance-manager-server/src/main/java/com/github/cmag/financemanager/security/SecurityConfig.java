package com.github.cmag.financemanager.security;

import com.github.cmag.financemanager.repository.UserRepository;
import com.github.cmag.financemanager.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${finance.manager.jwt.secret}")
  private String secret;

  @Value("${finance.manager.jwt.expires}")
  private int expires;

  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private UserDetailsServiceImpl userDetailsService;
  private UserRepository userRepository;

  public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder,
      UserDetailsServiceImpl userDetailsService, UserRepository userRepository) {
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.userDetailsService = userDetailsService;
    this.userRepository = userRepository;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests()
        .antMatchers("/auth/register", "/auth/forgotPassword").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilter(new AuthenticationFilter(authenticationManager(),secret, expires, userRepository))
        .addFilter(new AuthorizationFilter(authenticationManager(), secret));
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }
}