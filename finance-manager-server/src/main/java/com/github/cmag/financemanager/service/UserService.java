package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.mapper.UserMapper;
import com.github.cmag.financemanager.model.User;
import com.github.cmag.financemanager.repository.UserRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The User Service.
 */
@Service
@Transactional
public class UserService extends BaseService<UserDTO, User> {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMapper mapper;

  /**
   * Find the User with the given username.
   *
   * @param username The username.
   * @return The User.
   */
  public UserDTO findByUsername(String username) {
    return mapper.map(userRepository.findByUsername(username));
  }

  /**
   * Find the User with the given email.
   *
   * @param email The email.
   * @return The User.
   */
  public UserDTO findByEmail(String email) {
    return mapper.map(userRepository.findByEmail(email));
  }

  /**
   * Get the currently logged in user.
   *
   * @return The User.
   */
  public User getLoggedInUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!Objects.isNull(authentication)) {
      return userRepository.findByUsername(authentication.getName());
    }
    return null;
  }

  /**
   * Get the currently logged in user DTO.
   *
   * @return The User DTO.
   */
  public UserDTO getLoggedInUserDTO() {
    return mapper.map(getLoggedInUser());
  }
}
