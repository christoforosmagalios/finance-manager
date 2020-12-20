package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.repository.UserRepository;
import java.util.ArrayList;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of the UserDetailsService.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  /**
   * Override loadUserByUsername method in order to find the User with username or by email.
   *
   * @param username Contains the username or the email.
   * @return The UserDetails.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Find the user by username or email.
    com.github.cmag.financemanager.model.User user = userRepository
        .findByUsernameOrEmail(username, username);
    // If no user has been found, throw exception.
    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException(username);
    }
    return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
  }
}
