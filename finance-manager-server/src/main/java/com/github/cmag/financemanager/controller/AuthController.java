package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.ForgotPasswordDTO;
import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.dto.UserDetailsDTO;
import com.github.cmag.financemanager.dto.UserPasswordDetailsDTO;
import com.github.cmag.financemanager.service.PasswordResetService;
import com.github.cmag.financemanager.service.UserService;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public void forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO,
      HttpServletRequest request) {
    passwordResetService
        .forgotPassword(forgotPasswordDTO.getEmail(), forgotPasswordDTO.getLanguage(), request);
  }

  /**
   * Validate the given encrypted reset password id.
   *
   * @param id The encrypted id to be validated.
   * @return true if the given encrypted id is a valid one, false otherwise.
   */
  @GetMapping("/validateResetPassword")
  public boolean validateResetPassword(@PathParam("id") String id) {
    UserDTO user = passwordResetService.validateResetPasswordId(id);
    return !Objects.isNull(user);
  }

  /**
   * Change the password for the user.
   *
   * @param userPasswordDetailsDTO Contains the user's password information.
   */
  @PostMapping("/changePassword")
  public void changePassword(@Valid @RequestBody UserPasswordDetailsDTO userPasswordDetailsDTO) {
    passwordResetService.changePassword(userPasswordDetailsDTO);
  }
}
