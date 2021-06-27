package com.github.cmag.financemanager.util.validation;

import com.github.cmag.financemanager.dto.ForgotPasswordDTO;
import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.service.UserService;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Constrain Validator. Check that the email exists.
 */
public class EmailExistsValidator implements ConstraintValidator<EmailExists, ForgotPasswordDTO> {

  @Autowired
  private UserService userService;
  private String field;
  private String message;

  /**
   * Initialize the constrain validator.
   */
  @Override
  public void initialize(EmailExists uniqueEmail) {
    message = uniqueEmail.message();
    field = uniqueEmail.field();
  }

  /**
   * Perform the validation check. If the given email exists return true.
   *
   * @param forgotPassword ForgotPasswordDTO.
   * @param context The validator context.
   * @return true if the email exists, false otherwise.
   */
  @Override
  public boolean isValid(ForgotPasswordDTO forgotPassword, ConstraintValidatorContext context) {
    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode(field)
        .addConstraintViolation()
        .disableDefaultConstraintViolation();

    UserDTO userDTO = userService.findByEmail(forgotPassword.getEmail());
    return !Objects.isNull(userDTO);
  }
}
