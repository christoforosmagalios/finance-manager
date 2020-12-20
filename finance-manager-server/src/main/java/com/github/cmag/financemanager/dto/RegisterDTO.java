package com.github.cmag.financemanager.dto;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.util.validation.PasswordsMatch;
import com.github.cmag.financemanager.util.validation.UniqueEmail;
import com.github.cmag.financemanager.util.validation.UniqueUsername;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * Contains info about the user to be registered.
 */
@Data
@PasswordsMatch
public class RegisterDTO {

  @NotBlank
  @UniqueUsername
  @Size(min = 3, message = AppConstants.MIN_SIZE_3)
  private String username;

  @NotBlank
  @Size(min = 3, message = AppConstants.MIN_SIZE_3)
  private String firstName;

  @NotBlank
  @Size(min = 3, message = AppConstants.MIN_SIZE_3)
  private String lastName;

  @NotBlank
  @Email(message = AppConstants.INVALID_EMAIL, regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")
  @UniqueEmail
  private String email;

  @NotBlank
  @Size(min = 6, message = AppConstants.MIN_SIZE_6)
  private String password;

  @NotBlank
  @Size(min = 6, message = AppConstants.MIN_SIZE_6)
  private String confirmPassword;
}
