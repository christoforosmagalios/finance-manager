package com.github.cmag.financemanager.util.validation;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.UserPasswordDetailsDTO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Custom Constrain Validator. Check that the password and the confirmation password match.
 */
public class PasswordsMatchValidator implements
    ConstraintValidator<PasswordsMatch, UserPasswordDetailsDTO> {

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
   * @param user UserPasswordDetailsDTO.
   * @param context The validator context.
   * @return true if valid, false otherwise.
   */
  @Override
  public boolean isValid(UserPasswordDetailsDTO user, ConstraintValidatorContext context) {

    boolean isChangePassword = user.isChangePassword();
    boolean isNewUser = StringUtils.isBlank(user.getId());
    String password = user.getPassword() != null ? user.getPassword() : "";
    String confirmPassword =
        user.getConfirmPassword() != null ? user.getConfirmPassword() : "";

    if ((isNewUser || isChangePassword)
        && (StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)
        || password.length() < 6 || confirmPassword.length() < 6)) {
      context.buildConstraintViolationWithTemplate(AppConstants.MIN_SIZE_6).addPropertyNode(field)
          .addConstraintViolation().disableDefaultConstraintViolation();
      return false;
    } else if (!isNewUser && !isChangePassword) {
      return true;
    }
    context.buildConstraintViolationWithTemplate(message).addPropertyNode(field)
        .addConstraintViolation().disableDefaultConstraintViolation();

    return password.equals(confirmPassword);
  }
}
