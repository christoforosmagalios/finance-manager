package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.RegisterDTO;
import com.github.cmag.financemanager.service.AuthService;
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
  private AuthService authService;

  /**
   * Register the given user.
   *
   * @param registerDTO Contains registration info.
   */
  @PostMapping("/register")
  public void register(@Valid @RequestBody RegisterDTO registerDTO) {
    authService.register(registerDTO);
  }
}
