package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.dto.UserDetailsDTO;
import com.github.cmag.financemanager.mapper.UserMapper;
import com.github.cmag.financemanager.model.User;
import com.github.cmag.financemanager.repository.UserRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

  @Autowired
  private BCryptPasswordEncoder encoder;

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

  /**
   * Get the id of the logged in user.
   *
   * @return The id.
   */
  public String getLoggedInUserId() {
    return getLoggedInUser().getId();
  }

  /**
   * Encode the given password and save the user.
   *
   * @param userDetailsDTO Contains information about the user to be saved.
   */
  public void create(UserDetailsDTO userDetailsDTO) {
    userDetailsDTO.setPassword(encoder.encode(userDetailsDTO.getPassword()));
    userRepository.save(mapper.map(userDetailsDTO));
  }

  /**
   * Save the given user details.
   *
   * @param userDetailsDTO The user details.
   * @return The saved user dto representation.
   */
  public UserDTO update(UserDetailsDTO userDetailsDTO) {
    // Fetch the user from the database.
    User user = userRepository.getOne(userDetailsDTO.getId());
    // Update only the allowed fields.
    user.setEmail(userDetailsDTO.getEmail());
    user.setFirstName(userDetailsDTO.getFirstName());
    user.setLastName(userDetailsDTO.getLastName());
    // Update the password in case the user selected this option.
    if (userDetailsDTO.isChangePassword()) {
      user.setPassword(encoder.encode(userDetailsDTO.getPassword()));
    }
    // Update and return the user.
    return mapper.map(userRepository.save(user));
  }
}
