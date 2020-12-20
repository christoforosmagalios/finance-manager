package com.github.cmag.financemanager.util.validation;

import com.github.cmag.financemanager.dto.RegisterDTO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Custom Constrain Validator. Check that the password and the confirmation password match.
 */
public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, RegisterDTO> {

  private String field;
  private String message;

  /**
   * Initialize the constrain validator.
   *
   * @param passwordsMatch PasswordsMatch
   */
  @Override
  public void initialize(PasswordsMatch passwordsMatch) {
    message = passwordsMatch.message();
    field = passwordsMatch.field();
  }

  /**
   * Perform the validation check. If the password does not match with the confirmation password
   * return false.
   *
   * @param registerDTO The register dto.
   * @param context The validator context.
   * @return true if valid, false otherwise.
   */
  @Override
  public boolean isValid(RegisterDTO registerDTO, ConstraintValidatorContext context) {
    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode(field)
        .addConstraintViolation()
        .disableDefaultConstraintViolation();

    return registerDTO.getPassword().equals(registerDTO.getConfirmPassword());
  }
}
