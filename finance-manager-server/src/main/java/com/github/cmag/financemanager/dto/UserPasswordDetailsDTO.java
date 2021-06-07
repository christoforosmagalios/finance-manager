package com.github.cmag.financemanager.dto;

import com.github.cmag.financemanager.util.validation.PasswordsMatch;
import lombok.Data;

/**
 * Contains info about the user's password.
 */
@Data
@PasswordsMatch
public class UserPasswordDetailsDTO extends BaseDTO {

  private String password;
  private String confirmPassword;
  private boolean changePassword;
  private String resetPasswordId;
}
