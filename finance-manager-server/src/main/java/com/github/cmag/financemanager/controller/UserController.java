package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.dto.UserDetailsDTO;
import com.github.cmag.financemanager.model.User;
import com.github.cmag.financemanager.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Rest controller.
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<UserDTO, User> {

  @Autowired
  private UserService userService;

  /**
   * Get the currently logged in user details.
   *
   * @return User DTO representation.
   */
  @GetMapping("/current")
  public UserDTO getLoggedInUser() {
    return userService.getLoggedInUserDTO();
  }

  /**
   * Update the given user details.
   *
   * @param user Contains the user details.
   * @return User DTO representation.
   */
  @PostMapping("/update")
  public UserDTO update(@Valid @RequestBody UserDetailsDTO user) {
    return userService.update(user);
  }
}
