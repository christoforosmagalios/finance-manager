package com.github.cmag.financemanager.util.validation;

import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.service.UserService;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Constrain Validator. Check that the username is not already in use.
 */
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

  @Autowired
  private UserService userService;

  /**
   * Initialize the constrain validator.
   */
  @Override
  public void initialize(UniqueUsername uniqueUsername) {
  }

  /**
   * Perform the validation check. If the given username is already in use return false.
   *
   * @param username The username.
   * @param context The validator context.
   * @return true if valid, false otherwise.
   */
  @Override
  public boolean isValid(String username, ConstraintValidatorContext context) {
    UserDTO user = userService.findByUsername(username);
    return Objects.isNull(user) ? true : false;
  }
}
