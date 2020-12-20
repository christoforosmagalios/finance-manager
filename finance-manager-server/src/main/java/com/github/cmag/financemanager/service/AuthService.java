package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.dto.RegisterDTO;
import com.github.cmag.financemanager.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Auth Service.
 */
@Service
public class AuthService {

  @Autowired
  private UserService userService;

  @Autowired
  private UserMapper mapper;

  @Autowired
  private BCryptPasswordEncoder encoder;

  /**
   * Encode the given password and save the user.
   *
   * @param registerDTO Contains information about the user to be saved.
   */
  public void register(RegisterDTO registerDTO) {
    registerDTO.setPassword(encoder.encode(registerDTO.getPassword()));
    userService.save(mapper.map(registerDTO));
  }
}
