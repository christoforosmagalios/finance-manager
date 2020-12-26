package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.UserDetailsDTO;
import com.github.cmag.financemanager.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth Rest Controller.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserService userService;

  /**
   * Register the given user.
   *
   * @param userDetailsDTO Contains registration info.
   */
  @PostMapping("/register")
  public void register(@Valid @RequestBody UserDetailsDTO userDetailsDTO) {
    userService.create(userDetailsDTO);
  }
}
