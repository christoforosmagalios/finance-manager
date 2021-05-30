package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.ForgotPasswordDTO;
import com.github.cmag.financemanager.dto.UserDetailsDTO;
import com.github.cmag.financemanager.service.PasswordResetService;
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

  @Autowired
  private PasswordResetService passwordResetService;

  /**
   * Register the given user.
   *
   * @param userDetailsDTO Contains registration info.
   */
  @PostMapping("/register")
  public void register(@Valid @RequestBody UserDetailsDTO userDetailsDTO) {
    userService.create(userDetailsDTO);
  }

  /**
   * Send a new password request email.
   *
   * @param forgotPasswordDTO Contains the email and language.
   */
  @PostMapping("/forgotPassword")
  public void forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
    passwordResetService.forgotPassword(forgotPasswordDTO.getEmail(), forgotPasswordDTO.getLanguage());
  }
}
