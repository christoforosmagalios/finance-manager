package com.github.cmag.financemanager.util.validation;

import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.service.UserService;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Constrain Validator. Check that the email is not already in use.
 */
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

  @Autowired
  private UserService userService;

  /**
   * Initialize the constrain validator.
   */
  @Override
  public void initialize(UniqueEmail uniqueEmail) {
  }

  /**
   * Perform the validation check. If the given email is already in use return false.
   *
   * @param email The email.
   * @param context The validator context.
   * @return true if valid, false otherwise.
   */
  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    UserDTO user = userService.findByEmail(email);
    return Objects.isNull(user) ? true : false;
  }
}
