package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.mapper.UserMapper;
import com.github.cmag.financemanager.model.User;
import com.github.cmag.financemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

  public UserDTO findByUsername(String username) {
    return mapper.map(userRepository.findByUsername(username));
  }

  public UserDTO findByEmail(String email) {
    return mapper.map(userRepository.findByEmail(email));
  }
}
