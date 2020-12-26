package com.github.cmag.financemanager.util.validation;

import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.dto.UserDetailsDTO;
import com.github.cmag.financemanager.service.UserService;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import liquibase.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Constrain Validator. Check that the email is not already in use from another user.
 */
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, UserDetailsDTO> {

  @Autowired
  private UserService userService;
  private String field;
  private String message;

  /**
   * Initialize the constrain validator.
   */
  @Override
  public void initialize(UniqueEmail uniqueEmail) {
    message = uniqueEmail.message();
    field = uniqueEmail.field();
  }

  /**
   * Perform the validation check. If the given email is already in use from another user return false.
   *
   * @param user UserDetailsDTO.
   * @param context The validator context.
   * @return true if valid, false otherwise.
   */
  @Override
  public boolean isValid(UserDetailsDTO user, ConstraintValidatorContext context) {
    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode(field)
        .addConstraintViolation()
        .disableDefaultConstraintViolation();
    
    UserDTO userDTO = userService.findByEmail(user.getEmail());
    if (Objects.isNull(userDTO)
        || !StringUtils.isEmpty(user.getId()) && userDTO.getId().equals(user.getId())) {
      return true;
    }
    return false;
  }
}
