package com.github.cmag.financemanager.dto;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.util.validation.UniqueEmail;
import com.github.cmag.financemanager.util.validation.UniqueUsername;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * Contains info about the user.
 */
@Data
@UniqueUsername
@UniqueEmail
public class UserDetailsDTO extends UserPasswordDetailsDTO {

  @NotBlank
  @Size(min = 3, message = AppConstants.MIN_SIZE_3)
  private String username;

  @NotBlank
  @Size(min = 3, message = AppConstants.MIN_SIZE_3)
  private String firstName;

  @NotBlank
  @Size(min = 3, message = AppConstants.MIN_SIZE_3)
  private String lastName;

  @NotBlank
  @Email(message = AppConstants.INVALID_EMAIL)
  private String email;
}
